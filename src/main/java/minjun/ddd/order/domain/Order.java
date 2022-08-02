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
import minjun.ddd.order.domain.state.OrderState;
import minjun.ddd.order.domain.state.PlacedState;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryId", "paymentId"})
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  private Money totalAmount = Money.ZERO;

  private Long deliveryId;

  private Long paymentId;

  private OrderState state = new PlacedState();

  /**
   * order를 만저 만들고 delivery를 set할 것인가? delivery를 만들고 order를 생성할 것인가?
   */
  public static Order createOrder(OrderLine orderLine, Long paymentId) {
    final Money totalAmount = orderLine.calcTotalAmount();
    verifyMinimumTotalAmount(totalAmount);

    return new Order(orderLine, totalAmount, paymentId);
  }

  private Order(OrderLine orderLine, Money totalAmount, Long paymentId) {
    this.orderLine = orderLine;
    this.totalAmount = totalAmount;
    this.paymentId = paymentId;
  }

  public void changeState(OrderState newState) {
    this.state = newState;
  }

  public void approvePayment() {
    this.state.paymentApproved(this);
  }

  public void startDelivery() {
    this.state.deliveryStarted(this);
  }

  public void completeDelivery() {
    this.state.deliveryCompleted(this);
  }

  public void cancelOrder() {
    this.state.orderCanceled(this);
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
