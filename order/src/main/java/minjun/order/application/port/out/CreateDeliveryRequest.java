package minjun.order.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateDeliveryRequest {

  private Long orderId;
  private String zipCode;
  private String address;
  private String phoneNumber;
}
