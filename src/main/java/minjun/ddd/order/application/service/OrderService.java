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
    final Order order = getOrder(orderId);

    final Address address = command.getDeliveryInfo().getAddress();
    final Boolean responseFromDelivery = deliveryPort.changeDeliveryInfo(order.getDeliveryId(),
        address.getZipCode(), address.getAddress(), command.getDeliveryInfo().getPhoneNumber());
    if (!responseFromDelivery) {
      throw new RuntimeException("배송 정보 변경 실패");
    }
  }

  @Override
  public void cancelOrder(Long orderId) {
    final Order order = getOrder(orderId);

    final Boolean responseFromPayment = paymentPort.cancelPayment(order.getPaymentId());
    if (!responseFromPayment) {
      throw new RuntimeException("결제 취소 실패");
    }

    order.cancelOrder();
    orderEventPublisher.publish(new OrderCancelledEvent(order));
  }

  @Override
  public void completeDelivery(Long orderId) {
    final Order order = getOrder(orderId);
    order.completeDelivery();
  }

  public void approvePayment(Long orderId, Long paymentId) {
    final Order order = getOrder(orderId);
    order.approvePayment(paymentId);
  }

  public void startDelivery(Long orderId) {
    final Order order = getOrder(orderId);
    order.startDelivery();
  }

  private Order getOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(NoSuchElementException::new);
  }

//
//
//  @Transactional
//  public void createOrder(CreateOrderRequest requestDto) {
//    final Order order = OrderMapper.toOrder(requestDto);
//
//    orderRepository.save(order);
//  }
//
//  @Transactional
//  public void cancelOrder(Long orderId) {
//    final Order order = orderRepository.findById(orderId)
//        .orElseThrow(() -> new RuntimeException("Not Found OrderId: " + orderId));
//    order.cancelOrder();
//    orderRepository.save(order);
//  }
//
//  public void changeDeliveryInfo(Long orderId, DeliveryInfoRequest deliveryInfoRequest) {
//    final Order order = orderRepository.findById(orderId)
//        .orElseThrow(() -> new RuntimeException("Not Found OrderId: " + orderId));
//    order.changeDeliveryInfo(OrderMapper.toDeliveryInfo(deliveryInfoRequest));
//    orderRepository.save(order);
//  }
}
