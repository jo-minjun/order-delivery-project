package minjun.order.application.port;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DeliveryInfo {
  private Long deliveryId;
  private Address address;
  private String phoneNumber;
}
