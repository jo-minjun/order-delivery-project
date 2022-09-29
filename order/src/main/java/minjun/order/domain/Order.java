package minjun.order.domain;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.order.domain.state.OrderState;
import minjun.order.domain.state.PlacedState;
import minjun.sharedkernel.domain.Money;
import minjun.sharedkernel.domain.MoneyConverter;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryId", "paymentId"})
public class Order implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  @Convert(converter = MoneyConverter.class)
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
