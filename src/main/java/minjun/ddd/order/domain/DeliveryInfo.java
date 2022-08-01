package minjun.ddd.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import minjun.ddd.delivery.domain.Delivery;

@Embeddable
public class DeliveryInfo {

  private String phoneNumber;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deliveries_id")
  private Delivery delivery;
}
