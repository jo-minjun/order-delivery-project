package minjun.order.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import minjun.order.application.port.DeliveryInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderCommand {

  private String cardNo;
  private DeliveryInfo deliveryInfo;
}
