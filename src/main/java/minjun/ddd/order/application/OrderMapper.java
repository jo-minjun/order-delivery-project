package minjun.ddd.order.application;

import java.util.Set;
import java.util.stream.Collectors;
import minjun.ddd.common.domain.Money;
import minjun.ddd.order.application.port.CreateOrderRequest.OrderProduct;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.LineItem;
import minjun.ddd.order.domain.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public static OrderLine toOrderLine(Set<OrderProduct> orderProducts) {
    Set<LineItem> LineItems = orderProducts.stream()
        .map(OrderMapper::getLineItem)
        .collect(Collectors.toSet());

    return new OrderLine(LineItems);
  }

  private static LineItem getLineItem(OrderProduct orderProduct) {
    return new LineItem(
        orderProduct.getProductId(),
        new Money(orderProduct.getPrice()),
        orderProduct.getQuantity()
    );
  }

  public static DeliveryInfo toDeliveryInfo(String address, String zipCode, String phoneNumber) {
    return new DeliveryInfo(address, zipCode, phoneNumber);
  }
}
