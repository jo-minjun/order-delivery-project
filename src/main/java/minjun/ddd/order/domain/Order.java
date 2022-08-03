package minjun.ddd.order.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Money;
import minjun.ddd.common.domain.event.order.OrderCancelledEvent;
import minjun.ddd.common.domain.event.order.OrderCreatedEvent;
import minjun.ddd.common.domain.event.order.OrderDeliveryCompletedEvent;
import minjun.ddd.common.domain.event.order.OrderDeliveryStartedEvent;
import minjun.ddd.common.domain.event.order.OrderPaymentApprovedEvent;
import minjun.ddd.order.domain.state.OrderState;
import minjun.ddd.order.domain.state.PlacedState;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryInfo", "paymentId"})
public class Order extends AbstractAggregateRoot<Order> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  private Money totalAmount = Money.ZERO;

  @Embedded
  private DeliveryInfo deliveryInfo;

  private Long paymentId;

  // 디자인 패턴 학습을 위해 상태 패턴 사용
  private OrderState state = new PlacedState();

  public static Order createOrder(OrderLine orderLine, String cardNo, DeliveryInfo deliveryInfo) {
    final Money totalAmount = orderLine.calcTotalAmount();
    verifyMinimumTotalAmount(totalAmount);

    return new Order(orderLine, totalAmount, cardNo, deliveryInfo);
  }

  private Order(OrderLine orderLine, Money totalAmount, String cardNo, DeliveryInfo deliveryInfo) {
    this.orderLine = orderLine;
    this.totalAmount = totalAmount;
    this.deliveryInfo = deliveryInfo;

    this.registerEvent(new OrderCreatedEvent(this, cardNo, totalAmount));
  }

  public void associatePayment(Long paymentId) {
    this.paymentId = paymentId;
  }

  public void associateDelivery(Long deliveryId) {
    deliveryInfo.associateDeliveryId(deliveryId);
  }

  public void changeState(OrderState newState) {
    this.state = newState;
  }

  public void approvePayment() {
    this.state.paymentApproved(this);

    this.registerEvent(new OrderPaymentApprovedEvent(this));
  }

  public void startDelivery() {
    this.state.deliveryStarted(this);

    this.registerEvent(new OrderDeliveryStartedEvent(this));
  }

  public void completeDelivery() {
    this.state.deliveryCompleted(this);

    this.registerEvent(new OrderDeliveryCompletedEvent(this));
  }

  public void cancelOrder() {
    this.state.orderCanceled(this);

    this.registerEvent(new OrderCancelledEvent(this));
  }

  public boolean canChangeDeliveryInfo() {
    return this.state.canChangeDeliveryInfo();
  }

  private static void verifyMinimumTotalAmount(Money totalAmount) {
    final Money minimumOrderAmount = new Money(10000);

    if (!totalAmount.isGreaterThanOrEqualTo(minimumOrderAmount)) {
      throw new RuntimeException("Less than Minimum TotalAmount");
    }
  }
}
