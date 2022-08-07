package minjun.ddd;

import java.math.BigDecimal;
import java.util.Set;
import minjun.ddd.order.application.port.in.Address;
import minjun.ddd.order.application.port.in.ChangeOrderCommand;
import minjun.ddd.order.application.port.in.DeliveryInfo;
import minjun.ddd.order.application.port.in.OrderItem;
import minjun.ddd.order.application.port.in.PaymentInfo;
import minjun.ddd.order.application.port.in.PlaceOrderCommand;

public class Fixture {

  public static ChangeOrderCommand createChangeOrderCommand() {
    return new ChangeOrderCommand(createDeliveryInfo(2));
  }

  public static PlaceOrderCommand createPlaceOrderCommand(Boolean isOverMinumum) {
    return PlaceOrderCommand.builder()
        .orderItems(createOrderItems(isOverMinumum))
        .deliveryInfo(createDeliveryInfo(1))
        .paymentInfo(createPaymentInfo())
        .build();
  }

  public static PaymentInfo createPaymentInfo() {
    return PaymentInfo.builder()
        .cardNo("0000")
        .build();
  }

  public static DeliveryInfo createDeliveryInfo(int i) {
    return DeliveryInfo.builder()
        .address(Address.builder()
            .zipCode("대한민국 어딘가 우편번호" + i)
            .address("대한민국 어딘가 주소" + i)
            .build()
        )
        .phoneNumber("010-0000-0000")
        .build();
  }

  public static Set<OrderItem> createOrderItems(Boolean isOverMinimum) {
    if (isOverMinimum) {
      return Set.of(createOrderItem(1L, new BigDecimal(10000L), 1));
    }
    return Set.of(
        createOrderItem(1L, new BigDecimal(1000), 9),
        createOrderItem(1L, new BigDecimal(999), 1)
    );
  }

  public static OrderItem createOrderItem(Long id, BigDecimal price, Integer quantity) {
    return OrderItem.builder()
        .productId(id)
        .price(price)
        .quantity(quantity)
        .build();
  }
}
