package minjun.delivery.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryDto {

  private Long id;
  private String address;
  private String zipCode;
  private String phoneNumber;
  private String status;
  private Long orderId;
}
