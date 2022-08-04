package minjun.ddd.order.application.service;

import lombok.RequiredArgsConstructor;
import minjun.ddd.common.Money;
import minjun.ddd.order.application.port.out.*;
import minjun.ddd.order.application.port.in.*;
import minjun.ddd.order.domain.LineItem;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.OrderLine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements OrderUsecase {

  private final OrderRepository orderRepository;
  private final OrderEventPublisher orderEventPublisher;
  private final DeliveryPort deliveryPort;
  private final PaymentPort paymentPort;

  @Override
  public OrderDto getOrder(Long orderId) {
    final Order order = findOrder(orderId);
    final DeliveryInfo deliveryInfo = deliveryPort.getDelivery(order.getDeliveryId());
    final PaymentInfo paymentInfo = paymentPort.getPayment(order.getPaymentId());

    final Set<OrderItem> orderItems = order.getOrderLine().getLineItems().stream()
        .map(lineItem ->
            OrderItem.builder()
                .productId(lineItem.getProductId())
                .price(lineItem.getPrice().getValue())
                .quantity(lineItem.getQuantity())
                .build()
        )
        .collect(Collectors.toSet());
    return OrderDto.builder()
        .orderId(order.getId())
        .orderItems(orderItems)
        .totalAmount(order.getTotalAmount().getValue())
        .delivery(deliveryInfo)
        .payment(paymentInfo)
        .status(order.getState().getClass().getSimpleName())
        .build();
  }

  @Override
  public Long placeOrder(PlaceOrderCommand command) {
    // Order 생성
    final Set<LineItem> lineItems = command.getOrderItems().stream()
        .map(orderItem -> new LineItem(
            orderItem.getProductId(),
            new Money(orderItem.getPrice()),
            orderItem.getQuantity())
        )
        .collect(Collectors.toSet());

    final Order order = Order.placeOrder(new OrderLine(lineItems),
        command.getPaymentInfo().getCardNo());
    orderRepository.save(order);

    // Payment 생성 요청
    final Long paymentId = paymentPort.createPayment(
        order.getId(), command.getPaymentInfo().getCardNo(), order.getTotalAmount());
    order.approvePayment(paymentId);

    // Delivery 생성 요청
    final Address address = command.getDeliveryInfo().getAddress();
    final Long deliveryId = deliveryPort.createDelivery(
        order.getId(), address.getZipCode(), address.getAddress(), command.getDeliveryInfo().getPhoneNumber());
    order.associateDelivery(deliveryId);

    orderEventPublisher.publish(new OrderPlacedEvent(order, command));

    return order.getId();
  }

  @Override
  public void changeOrder(Long orderId, ChangeOrderCommand command) {
    final Order order = findOrder(orderId);

    final Address address = command.getDeliveryInfo().getAddress();
    final Boolean responseFromDelivery = deliveryPort.changeDeliveryInfo(order.getDeliveryId(),
        address.getZipCode(), address.getAddress(), command.getDeliveryInfo().getPhoneNumber());
    if (!responseFromDelivery) {
      throw new RuntimeException("배송 정보 변경 실패");
    }
  }

  @Override
  public void cancelOrder(Long orderId) {
    final Order order = findOrder(orderId);

    final Boolean responseFromPayment = paymentPort.cancelPayment(order.getPaymentId());
    if (!responseFromPayment) {
      throw new RuntimeException("결제 취소 실패");
    }

    order.cancelOrder();
    orderEventPublisher.publish(new OrderCancelledEvent(order));
  }

  @Override
  public void completeDelivery(Long orderId) {
    final Order order = findOrder(orderId);
    order.completeDelivery();
  }

  public void approvePayment(Long orderId, Long paymentId) {
    final Order order = findOrder(orderId);
    order.approvePayment(paymentId);
  }

  public void startDelivery(Long orderId) {
    final Order order = findOrder(orderId);
    order.startDelivery();
  }

  private Order findOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(NoSuchElementException::new);
  }
}
