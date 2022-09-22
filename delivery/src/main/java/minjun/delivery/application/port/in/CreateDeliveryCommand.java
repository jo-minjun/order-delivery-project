package minjun.delivery.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateDeliveryCommand {

  private Long orderId;
  private String zipCode;
  private String address;
  private String phoneNumber;
}
