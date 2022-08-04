package minjun.ddd.delivery.domain;

import java.io.Serializable;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "deliveries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "address", "status"})
public class Delivery implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private Address address;

  private String phoneNumber;

  @Enumerated(value = EnumType.STRING)
  private DeliveryStatus status = DeliveryStatus.SUBMITTED;

  private Long orderId;

  @Version
  private Integer version;

  public static Delivery createDelivery(Long orderId, Address address, String phoneNumber) {
    return new Delivery(orderId, address, phoneNumber);
  }

  private Delivery(Long orderId, Address address, String phoneNumber) {
    this.orderId = orderId;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public void changeDeliveryInfo(Address address, String phoneNumber) {
    canChangeDelivery();
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  private void canChangeDelivery() {
    if (!this.status.canChangeDelivery()) {
      throw new RuntimeException("배송 정보 수정 불가");
    }
  }
  // 생성 후 배송 API 요청시 배송 시작, 이벤트 발생 X

  public void startDelivery() {
    this.status = DeliveryStatus.STARTED;
  }

  public void completeDelivery() {
    this.status = DeliveryStatus.COMPLETED;
  }
}
