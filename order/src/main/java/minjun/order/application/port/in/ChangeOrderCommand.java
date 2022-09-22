package minjun.order.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import minjun.order.application.port.DeliveryInfo;

@Data
@AllArgsConstructor
public class ChangeOrderCommand {

  private DeliveryInfo deliveryInfo;
}
