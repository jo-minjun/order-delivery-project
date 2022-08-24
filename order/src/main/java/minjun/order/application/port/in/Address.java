package minjun.order.application.port.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
  private String zipCode;
  private String address;
}
