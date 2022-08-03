package minjun.ddd.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import minjun.ddd.common.domain.Address;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"address", "phoneNumber"})
@ToString(of = {"address", "phoneNumber"})
public class DeliveryInfo {

  private Long deliveryId;

  @Embedded
  private Address address;

  private String phoneNumber;

  public DeliveryInfo(String address, String zipCode, String phoneNumber) {
    this.address = new Address(zipCode, address);
    this.phoneNumber = phoneNumber;
  }

  void associateDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }
}
