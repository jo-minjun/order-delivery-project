package minjun.ddd.order.domain;

import lombok.*;
import minjun.ddd.common.Money;
import minjun.ddd.order.domain.state.OrderState;
import minjun.ddd.order.domain.state.PlacedState;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryId", "paymentId"})
public class Order implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  private Money totalAmount = Money.ZERO;

  private Long deliveryId;

  private Long paymentId;

  // 디자인 패턴 학습을 위해 상태 패턴 사용
  private OrderState state = new PlacedState();

  @Version
  private Integer version;

  public static Order placeOrder(OrderLine orderLine, String cardNo) {
    final Money totalAmount = orderLine.calcTotalAmount();
    verifyMinimumTotalAmount(totalAmount);

    return new Order(orderLine, totalAmount, cardNo);
  }

  private Order(OrderLine orderLine, Money totalAmount, String cardNo) {
    this.orderLine = orderLine;
    this.totalAmount = totalAmount;
  }

  public void associateDelivery(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

  public void changeState(OrderState newState) {
    this.state = newState;
  }

  public void approvePayment(Long paymentId) {
    this.paymentId = paymentId;
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

  private void verifyCanChangeDeliveryInfo() {
    if (!this.state.canChangeDeliveryInfo()) {
      throw new RuntimeException();
    }
  }

  private static void verifyMinimumTotalAmount(Money totalAmount) {
    final Money minimumOrderAmount = new Money(10000);

    if (!totalAmount.isGreaterThanOrEqualTo(minimumOrderAmount)) {
      throw new RuntimeException("Less than Minimum TotalAmount");
    }
  }
}
