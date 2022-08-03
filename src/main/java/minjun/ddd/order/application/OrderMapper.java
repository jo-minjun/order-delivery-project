package minjun.ddd.order.application;

import java.util.Set;
import java.util.stream.Collectors;
import minjun.ddd.common.domain.Address;
import minjun.ddd.common.domain.Money;
import minjun.ddd.order.application.port.CreateOrderRequest;
import minjun.ddd.order.application.port.CreateOrderRequest.DeliveryInfoRequest;
import minjun.ddd.order.application.port.CreateOrderRequest.OrderProductRequest;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.LineItem;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public static Order toOrder(CreateOrderRequest requestDto) {
    final OrderLine orderLine = OrderMapper.toOrderLine(requestDto.getOrderProducts());
    final DeliveryInfo deliveryInfo = OrderMapper.toDeliveryInfo(
        requestDto.getDeliveryInfo());

    return Order.createOrder(orderLine, requestDto.getCardNo(), deliveryInfo);
  }

  private static OrderLine toOrderLine(Set<OrderProductRequest> orderProducts) {
    Set<LineItem> LineItems = orderProducts.stream()
        .map(OrderMapper::getLineItem)
        .collect(Collectors.toSet());

    return new OrderLine(LineItems);
  }

  private static LineItem getLineItem(OrderProductRequest orderProduct) {
    return new LineItem(
        orderProduct.getProductId(),
        new Money(orderProduct.getPrice()),
        orderProduct.getQuantity()
    );
  }

  public static DeliveryInfo toDeliveryInfo(DeliveryInfoRequest deliveryInfo) {
    return new DeliveryInfo(
        toAddress(deliveryInfo.getAddress(), deliveryInfo.getZipCode()),
        deliveryInfo.getPhoneNumber()
    );
  }

  private static Address toAddress(String address, String zipCode) {
    return new Address(zipCode, address);
  }
}
