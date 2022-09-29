package minjun.order;

import java.math.BigDecimal;
import java.util.Set;
import minjun.order.application.port.Address;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.PaymentInfo;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderItem;
import minjun.order.application.port.in.PlaceOrderCommand;
import minjun.order.domain.LineItem;
import minjun.order.domain.Order;
import minjun.order.domain.OrderLine;
import minjun.order.domain.state.DeliveryStartedState;
import minjun.order.domain.state.PaymentApprovedState;
import minjun.order.domain.state.PlacedState;
import minjun.sharedkernel.domain.Money;

public class Fixture {

  public static Order getOrderPlaced() {
    Set<LineItem> lineItems = Set.of(
        new LineItem(1L, "pencil", new Money(1500), 2),
        new LineItem(2L, "macbook air", new Money(1_500_000), 1),
        new LineItem(3L, "keyboard", new Money(135_000), 1)
    );
    OrderLine orderLine = new OrderLine(lineItems);

    return new Order(
        1L,
        orderLine,
        orderLine.calcTotalAmount(),
        1L,
        1L,
        new PlacedState(),
        0
    );
  }

  public static Order getOrderDeliveryStarted() {
    final Order orderPlaced = getOrderPlaced();
    return new Order(
        orderPlaced.getId(),
        orderPlaced.getOrderLine(),
        orderPlaced.getTotalAmount(),
        orderPlaced.getDeliveryId(),
        orderPlaced.getPaymentId(),
        new DeliveryStartedState(),
        0
    );
  }

  public static Order getOrderPaymentApproved() {
    final Order orderPlaced = getOrderPlaced();
    return new Order(
        orderPlaced.getId(),
        orderPlaced.getOrderLine(),
        orderPlaced.getTotalAmount(),
        orderPlaced.getDeliveryId(),
        orderPlaced.getPaymentId(),
        new PaymentApprovedState(),
        0
    );
  }

  public static DeliveryInfo getDeliveryInfo() {
    return DeliveryInfo.builder()
        .deliveryId(1L)
        .address(Address.builder()
            .address("서울특별시 테헤란로")
            .zipCode("00001")
            .build())
        .phoneNumber("010-0000-0000")
        .build();
  }

  public static PaymentInfo getPaymentInfo() {
    return PaymentInfo.builder()
        .paymentId(1L)
        .cardNo("0000-1111-2222-3333")
        .authorizedNo("0123456789")
        .status("APPROVED")
        .build();
  }

  public static PlaceOrderCommand getPlaceOrderCommand(Integer totalAmount) {
    return PlaceOrderCommand.builder()
        .orderItems(Set.of(
            OrderItem.builder()
                .productId(1L)
                .price(BigDecimal.valueOf(totalAmount))
                .productName("something")
                .quantity(1)
                .build()
        ))
        .deliveryInfo(getDeliveryInfo())
        .paymentInfo(getPaymentInfo())
        .build();
  }

  public static ChangeOrderCommand getChangeOrderCommand() {
    return ChangeOrderCommand.builder()
        .deliveryInfo(getDeliveryInfo())
        .build();
  }
}
