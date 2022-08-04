package minjun.ddd.order.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryInfo {
  private Address address;
  private String phoneNumber;
}
