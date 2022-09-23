package minjun.order.application;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.order.application.port.Address;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.PaymentInfo;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderItem;
import minjun.order.application.port.in.OrderReactiveUsecase;
import minjun.order.application.port.in.PlaceOrderCommand;
import minjun.order.application.port.out.DeliveryReactivePort;
import minjun.order.application.port.out.OrderCancelledEvent;
import minjun.order.application.port.out.OrderEventPublisher;
import minjun.order.application.port.out.OrderPlacedEvent;
import minjun.order.application.port.out.OrderRepository;
import minjun.order.application.port.out.PaymentReactivePort;
import minjun.order.domain.LineItem;
import minjun.order.domain.Order;
import minjun.order.domain.OrderLine;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService implements OrderReactiveUsecase {

  private final OrderEventPublisher orderEventPublisher;
  private final OrderRepository orderRepository;
  private final DeliveryReactivePort deliveryPort;
  private final PaymentReactivePort paymentPort;

  @Override
  public Mono<OrderDto> getOrder(Long orderId) {
    return Mono.fromFuture(findOrder(orderId))
        .flatMap(order -> {
          final var deliveryInfo = deliveryPort.getDelivery(order.getDeliveryId());
          final var paymentInfo = paymentPort.getPayment(order.getPaymentId());

          return toOrderDto(order, deliveryInfo, paymentInfo);
        });
  }

  @Override
  public Mono<Long> placeOrder(PlaceOrderCommand command) {
    return createOrder(command)
        .flatMap(order -> {
          orderEventPublisher.publish(new OrderPlacedEvent(order, command));

          return Mono.fromFuture(supplyAsync(() -> orderRepository.save(order)))
              .flatMap(o -> createPayment(command, order).then(Mono.just(o)))
              .flatMap(o -> createDelivery(command, order).then(Mono.just(o)))
              .flatMap(o -> Mono.just(o.getId()));
        });
  }

  @Override
  public Mono<Void> changeOrder(Long orderId, ChangeOrderCommand command) {
    return Mono.fromFuture(findOrder(orderId))
        .flatMap(order -> changeDelivery(command, order))
        .then();
  }

  @Override
  public Mono<Void> startDelivery(Long orderId) {
    return Mono.fromFuture(findOrder(orderId))
        .doOnNext(Order::startDelivery)
        .then();
  }

  @Override
  public Mono<Void> completeDelivery(Long orderId) {
    return Mono.fromFuture(findOrder(orderId))
        .doOnNext(Order::completeDelivery)
        .then();
  }

  @Override
  public Mono<Void> cancelOrder(Long orderId) {
    return Mono.fromFuture(findOrder(orderId))
        .flatMap(this::cancelPayment)
        .then();
  }

  private Mono<Void> cancelPayment(Order order) {
    return paymentPort.cancelPayment(order.getPaymentId())
        .doOnNext(isSuccess -> {
          if (isSuccess) {
            order.cancelOrder();
            orderEventPublisher.publish(new OrderCancelledEvent(order));
          } else {
            throw new RuntimeException("결제 취소 실패");
          }
        })
        .then();
  }

  private Mono<OrderDto> toOrderDto(
      Order order,
      Mono<DeliveryInfo> deliveryInfo,
      Mono<PaymentInfo> paymentInfo
  ) {
    return Mono.zip(deliveryInfo, paymentInfo)
        .map(tuple -> OrderDto.builder()
            .orderId(order.getId())
            .orderItems(order.getOrderLine().getLineItems().stream()
                .map(lineItem -> OrderItem.builder()
                    .productId(lineItem.getProductId())
                    .price(lineItem.getPrice().getValue())
                    .quantity(lineItem.getQuantity())
                    .build())
                .collect(Collectors.toSet()))
            .totalAmount(order.getTotalAmount().getValue())
            .delivery(tuple.getT1())
            .payment(tuple.getT2())
            .status(order.getState().getClass().getSimpleName())
            .build());
  }

  private Mono<Void> createDelivery(PlaceOrderCommand command, Order order) {
    final Address address = command.getDeliveryInfo().getAddress();

    return deliveryPort.createDelivery(
            order.getId(),
            address.getZipCode(),
            address.getAddress(),
            command.getDeliveryInfo().getPhoneNumber()
        )
        .doOnNext(order::associateDelivery)
        .then();
  }

  private Mono<Void> createPayment(PlaceOrderCommand command, Order order) {
    return paymentPort.createPayment(
            order.getId(),
            command.getPaymentInfo().getCardNo(),
            order.getTotalAmount()
        )
        .doOnNext(order::approvePayment)
        .then();
  }

  private Mono<Order> createOrder(PlaceOrderCommand command) {
    return Mono.just(command.getOrderItems().stream()
            .map(orderItem -> new LineItem(
                orderItem.getProductId(),
                orderItem.getProductName(),
                new Money(orderItem.getPrice()),
                orderItem.getQuantity()
            ))
            .collect(Collectors.toSet()))
        .map(lineItems ->
            Order.placeOrder(new OrderLine(lineItems), command.getPaymentInfo().getCardNo()));
  }

  private Mono<Void> changeDelivery(ChangeOrderCommand command, Order order) {
    final Address address = command.getDeliveryInfo().getAddress();

    return deliveryPort.changeDeliveryInfo(
            order.getDeliveryId(),
            address.getZipCode(),
            address.getAddress(),
            command.getDeliveryInfo().getPhoneNumber()
        )
        .doOnNext(isSuccess -> {
          if (!isSuccess) {
            throw new RuntimeException("배송 정보 변경 실패");
          }
        })
        .then();
  }

  private CompletableFuture<Order> findOrder(Long orderId) {
    return supplyAsync(() -> orderRepository.findById(orderId)
        .orElseThrow(NoSuchElementException::new))
        .orTimeout(5000L, MILLISECONDS);
  }
}
