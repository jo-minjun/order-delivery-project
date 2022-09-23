package minjun.order.application.port.in;

import lombok.Builder;
import lombok.Data;
import minjun.order.application.port.DeliveryInfo;

@Data
@Builder
public class ChangeOrderCommand {

  private DeliveryInfo deliveryInfo;
}
