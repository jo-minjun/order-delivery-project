package minjun.ddd.payment.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 16)
  private String cardNo;

  @Enumerated(value = EnumType.STRING)
  private PaymentStatus status = PaymentStatus.SUBMITTED;

  private String authorizedNo;

  private Long orderId;

  public static Payment createPayment(Long orderId, String cardNo) {
    return new Payment(orderId, cardNo);
  }

  private Payment(Long orderId, String cardNo) {
    this.orderId = orderId;
    this.cardNo = cardNo;
  }

  public void approvePayment(String authorizedNo) {
    this.status = PaymentStatus.APPROVED;
    this.authorizedNo = authorizedNo;
  }

  public void rejectPayment() {
    this.status = PaymentStatus.REJECTED;
  }

  public void cancelPayment() {
    this.status = PaymentStatus.CANCELLED;
  }
}
