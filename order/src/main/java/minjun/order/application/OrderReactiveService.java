package minjun.order.application;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import minjun.order.application.port.Address;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderItem;
import minjun.order.application.port.in.OrderReactiveUsecase;
import minjun.order.application.port.in.PlaceOrderCommand;
import minjun.order.application.port.out.DeliveryReactivePort;
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
@RequiredArgsConstructor
@Transactional
public class OrderReactiveService implements OrderReactiveUsecase {

  private final OrderEventPublisher orderEventPublisher;
  private final OrderRepository orderRepository;
  private final DeliveryReactivePort deliveryPort;
  private final PaymentReactivePort paymentPort;

  @Override
  public Mono<OrderDto> getOrder(Long orderId) {
    final CompletableFuture<Order> orderFuture = CompletableFuture
        .supplyAsync(() -> findOrder(orderId))
        .orTimeout(5000L, MILLISECONDS);

    return Mono.fromFuture(orderFuture)
        .flatMap(order -> Mono.just(order.getDeliveryId())
                .flatMap(deliveryPort::getDelivery)
                .zipWith(Mono.just(order.getPaymentId()).flatMap(paymentPort::getPayment))
                .zipWith(Mono.just(order.getOrderLine().getLineItems().stream()
                    .map(lineItem -> OrderItem.builder()
                        .productId(lineItem.getProductId())
                        .price(lineItem.getPrice().getValue())
                        .quantity(lineItem.getQuantity())
                        .build())
                    .collect(Collectors.toSet())))
                .map(tuple -> OrderDto.builder()
                    .orderId(order.getId())
                    .orderItems(tuple.getT2())
                    .totalAmount(order.getTotalAmount().getValue())
                    .delivery(tuple.getT1().getT1())
                    .payment(tuple.getT1().getT2())
                    .status(order.getState().getClass().getSimpleName())
                    .build()));
  }

  @Override
  public Mono<Long> placeOrder(PlaceOrderCommand command) {
    return Mono.just(command.getOrderItems().stream()
            .map(orderItem -> new LineItem(orderItem.getProductId(), orderItem.getProductName(),
                new Money(orderItem.getPrice()), orderItem.getQuantity()))
            .collect(Collectors.toSet()))
        .map(lineItems -> Order.placeOrder(new OrderLine(lineItems),
            command.getPaymentInfo().getCardNo()))
        .doOnNext(order -> CompletableFuture.runAsync(() -> orderRepository.save(order)))
        .doOnNext(order -> paymentPort.createPayment(order.getId(),
                command.getPaymentInfo().getCardNo(), order.getTotalAmount())
            .doOnNext(paymentId ->
                CompletableFuture.runAsync(() -> order.approvePayment(paymentId))))
        .doOnNext(order -> {
          final Address address = command.getDeliveryInfo().getAddress();
          deliveryPort.createDelivery(order.getId(), address.getZipCode(), address.getAddress(),
                  command.getDeliveryInfo().getPhoneNumber())
              .doOnNext(deliveryId ->
                  CompletableFuture.runAsync(() -> order.associateDelivery(deliveryId)));
        })
        .map(order -> {
          orderEventPublisher.publish(new OrderPlacedEvent(order, command));
          return order.getId();
        });
  }

  @Override
  public void changeOrder(Long orderId, ChangeOrderCommand command) {

  }

  @Override
  public void startDelivery(Long orderId) {

  }

  @Override
  public void completeDelivery(Long orderId) {

  }

  @Override
  public void cancelOrder(Long orderId) {

  }

  private Order findOrder(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(NoSuchElementException::new);
  }
}
