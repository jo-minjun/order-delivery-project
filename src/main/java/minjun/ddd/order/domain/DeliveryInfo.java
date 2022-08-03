package minjun.ddd.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Address;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"address", "phoneNumber"})
@ToString(of = {"address", "phoneNumber"})
public class DeliveryInfo {

  private Long deliveryId;

  @Embedded
  private Address address;

  private String phoneNumber;

  public DeliveryInfo(Address address, String phoneNumber) {
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  void associateDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }
}
