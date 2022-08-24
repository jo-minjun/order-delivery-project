package minjun.order.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryInfo {
  private Long deliveryId;
  private Address address;
  private String phoneNumber;
}
