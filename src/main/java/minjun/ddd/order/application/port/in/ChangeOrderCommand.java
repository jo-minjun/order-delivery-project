package minjun.ddd.order.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeOrderCommand {

  private DeliveryInfo deliveryInfo;
}
