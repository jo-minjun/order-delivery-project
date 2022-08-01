package minjun.ddd.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Money;
import minjun.ddd.payment.domain.Payment;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "orderLine", "totalAmount", "deliveryInfo", "payment"})
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private OrderLine orderLine;

  @Embedded
  @AttributeOverrides(value = {
      @AttributeOverride(name = "value", column = @Column(name = "total_amount", nullable = false))
  })
  private Money totalAmount;

  private DeliveryInfo deliveryInfo;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payments_id")
  private Payment payment;

  @Enumerated(value = EnumType.STRING)
  private OrderStatus status;
}
