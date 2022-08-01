package minjun.ddd.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import minjun.ddd.common.domain.Address;

@Embeddable
@EqualsAndHashCode(of = {"deliveryId", "phoneNumber", "address"})
@ToString(of = {"deliveryId", "phoneNumber", "address"})
public class DeliveryInfo {

  private Long deliveryId;

  private String phoneNumber;

  @Embedded
  private Address address;

  @Builder
  public DeliveryInfo(Long deliveryId, String phoneNumber, Address address) {
    this.deliveryId = deliveryId;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }
}
