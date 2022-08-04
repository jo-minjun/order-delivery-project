package minjun.ddd.payment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import minjun.ddd.common.domain.event.PaymentEvent;
import minjun.ddd.common.domain.event.order.OrderCreatedEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Payment extends AbstractAggregateRoot<Payment> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 16)
  private String cardNo;

  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status = PaymentStatus.SUBMITTED;

  private String authorizedNo;

  public static Payment createPayment(OrderCreatedEvent event) {
    return new Payment(event.getCardNo());
  }

  private Payment(String cardNo) {
    this.cardNo = cardNo;
  }

  public void approvedPayment(String authorizedNo, Long orderId) {
    this.status = PaymentStatus.APPROVED;
    this.authorizedNo = authorizedNo;

    this.registerEvent(new PaymentEvent(this, orderId));
  }

  public void rejectedPayment(Long orderId) {
    this.status = PaymentStatus.REJECTED;

    this.registerEvent(new PaymentEvent(this, orderId));
  }
}
