package minjun.ddd.delivery.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Address;
import minjun.ddd.common.domain.event.DeliveryEvent;
import minjun.ddd.common.domain.event.order.OrderPaymentApprovedEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "address", "status"})
public class Delivery extends AbstractAggregateRoot<Delivery> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private Address address;

  private String phoneNumber;

  @Enumerated(value = EnumType.STRING)
  private DeliveryStatus status = DeliveryStatus.SUBMITTED;

  public void changeDeliveryInfo(Address address, String phoneNumber) {
    verifyNotYetDeliver();
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  private void verifyNotYetDeliver() {
    if (!this.status.equals(DeliveryStatus.SUBMITTED)) {
      throw new RuntimeException();
    }
  }

  public static Delivery createDelivery(OrderPaymentApprovedEvent event) {
    return new Delivery(event.getAddress(), event.getPhoneNumber());
  }

  // 생성 후 배송 API 요청시 배송 시작, 이벤트 발생 X
  private Delivery(Address address, String phoneNumber) {
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public void startDelivery(Long orderId) {
    this.status = DeliveryStatus.STARTED;

    this.registerEvent(new DeliveryEvent(this, orderId));
  }

  public void completeDelivery(Long orderId) {
    this.status = DeliveryStatus.COMPLETED;

    this.registerEvent(new DeliveryEvent(this, orderId));
  }
}
