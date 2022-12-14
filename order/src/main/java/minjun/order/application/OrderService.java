package minjun.order.application;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minjun.order.application.port.Address;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.PaymentInfo;
import minjun.order.application.port.in.ChangeOrderCommand;
import minjun.order.application.port.in.OrderDto;
import minjun.order.application.port.in.OrderItem;
import minjun.order.application.port.in.OrderUsecase;
import minjun.order.application.port.in.PayOrderCommand;
import minjun.order.application.port.in.PlaceOrderCommand;
import minjun.order.application.port.out.DeliveryReactivePort;
import minjun.order.application.port.out.OrderCancelledEvent;
import minjun.order.application.port.out.OrderEventPublisher;
import minjun.order.application.port.out.OrderRepository;
import minjun.order.application.port.out.PaymentReactivePort;
import minjun.order.domain.LineItem;
import minjun.order.domain.Order;
import minjun.order.domain.OrderLine;
import minjun.sharedkernel.domain.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService implements OrderUsecase {

  private final OrderEventPublisher orderEventPublisher;
  private final OrderRepository orderRepository;
  private final DeliveryReactivePort deliveryPort;
  private final PaymentReactivePort paymentPort;

  @Override
  public Mono<Long> placeOrder(PlaceOrderCommand command) {
    return createOrder(command)
        .flatMap(order ->
            Mono.fromCompletionStage(supplyAsync(() -> orderRepository.save(order)))
                .flatMap(o -> Mono.just(o.getId()))
        );
  }

  // TODO: User service (order schema??? userId??? user ????????? ?????? id ??????, command?????? deliveryInfo ??????)
  @Override
  public Mono<Boolean> payOrder(Long orderId, PayOrderCommand command) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .flatMap(order ->
            paymentPort.createPayment(order.getId(), command.getCardNo(), order.getTotalAmount())
                .flatMap(paymentInfo -> {
                  if (paymentInfo.getStatus().equals("APPROVED")) {
                    order.approvePayment(paymentInfo.getPaymentId());

                    return deliveryPort.createDelivery(
                            orderId,
                            command.getDeliveryInfo().getAddress().getZipCode(),
                            command.getDeliveryInfo().getAddress().getAddress(),
                            command.getDeliveryInfo().getPhoneNumber()
                        )
                        .then(Mono.just(true));
                  }
                  return Mono.just(false);
                })
        );
  }

  @Override
  public Mono<OrderDto> getOrder(Long orderId) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .flatMap(order -> {
          final var deliveryInfo = deliveryPort.getDelivery(order.getId());
          final var paymentInfo = paymentPort.getPayment(order.getId());

          return toOrderDto(order, deliveryInfo, paymentInfo);
        });
  }

  @Override
  public Mono<Void> changeOrder(Long orderId, ChangeOrderCommand command) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .flatMap(order -> changeDeliveryInfo(command, order));
  }

  @Override
  public Mono<Void> startDelivery(Long orderId) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .doOnNext(Order::startDelivery)
        .then();
  }

  @Override
  public Mono<Void> completeDelivery(Long orderId) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .doOnNext(Order::completeDelivery)
        .then();
  }

  // TODO: ????????? ????????? (Delivery) ??????
  @Override
  public Mono<Void> cancelOrder(Long orderId) {
    return Mono.fromCompletionStage(findOrder(orderId))
        .flatMap(this::cancelPayment);
  }

  private Mono<Order> associateInfo(Order order, Mono<Long> paymentIdMono) {
    return paymentIdMono
        .map(paymentId -> {
          order.approvePayment(paymentId);
          return order;
        })
        .publishOn(Schedulers.boundedElastic())
        .map(o -> {
          try {
            return supplyAsync(() -> orderRepository.save(o)).get();
          } catch (InterruptedException | ExecutionException e) {
            log.error("?????? ?????? ????????? ??????????????????: {}", o);
            throw new RuntimeException("?????? ????????? ?????? ??????");
          }
        });
  }

  private Mono<Void> cancelPayment(Order order) {
    return paymentPort.cancelPayment(order.getId())
        .flatMap(isSuccess -> {
          if (isSuccess) {
            order.cancelOrder();
            orderEventPublisher.publish(new OrderCancelledEvent(order));
            return Mono.empty();
          }
          return Mono.error(new RuntimeException("?????? ?????? ??????"));
        });
  }

  private Mono<OrderDto> toOrderDto(
      Order order,
      Mono<DeliveryInfo> deliveryInfo,
      Mono<PaymentInfo> paymentInfo
  ) {
    final Set<OrderItem> orderItems = order.getOrderLine().getLineItems().stream()
        .map(lineItem -> OrderItem.builder()
            .productId(lineItem.getProductId())
            .price(lineItem.getPrice().getValue())
            .quantity(lineItem.getQuantity())
            .build())
        .collect(Collectors.toSet());

    return Mono.zip(deliveryInfo, paymentInfo)
        .map(tuple -> OrderDto.builder()
            .orderId(order.getId())
            .orderItems(orderItems)
            .totalAmount(order.getTotalAmount().getValue())
            .delivery(tuple.getT1())
            .payment(tuple.getT2())
            .status(order.getState().getClass().getSimpleName())
            .build());
  }

  private Mono<Order> createOrder(PlaceOrderCommand command) {
    final Set<LineItem> orderItems = command.getOrderItems().stream()
        .map(orderItem -> new LineItem(
            orderItem.getProductId(),
            orderItem.getProductName(),
            new Money(orderItem.getPrice()),
            orderItem.getQuantity()
        ))
        .collect(Collectors.toSet());

    return Mono.just(orderItems)
        .map(lineItems -> Order.placeOrder(getRandomUserId(), new OrderLine(lineItems)));
  }

  private Long getRandomUserId() {
    return Double.valueOf(Math.random()).longValue();
  }

  private Mono<Void> changeDeliveryInfo(ChangeOrderCommand command, Order order) {
    final Address address = command.getDeliveryInfo().getAddress();

    return deliveryPort.changeDeliveryInfo(
            order.getId(),
            address.getZipCode(),
            address.getAddress(),
            command.getDeliveryInfo().getPhoneNumber()
        )
        .flatMap(isSuccess ->
            isSuccess ? Mono.empty() : Mono.error(new RuntimeException("?????? ?????? ?????? ??????")));
  }

  private CompletableFuture<Order> findOrder(Long orderId) {
    return supplyAsync(
        () -> orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new)
    ).orTimeout(5000L, MILLISECONDS);
  }
}
