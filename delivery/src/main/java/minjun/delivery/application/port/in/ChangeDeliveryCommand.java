package minjun.delivery.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeDeliveryCommand {

  private String zipCode;
  private String address;
  private String phoneNumber;
}
