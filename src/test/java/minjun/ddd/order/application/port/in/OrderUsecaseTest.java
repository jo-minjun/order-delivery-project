package minjun.ddd.order.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Set;
import minjun.ddd.delivery.application.port.in.DeliveryUsecase;
import minjun.ddd.order.application.port.out.OrderRepository;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.state.CancelledState;
import minjun.ddd.order.domain.state.PaymentApprovedState;
import minjun.ddd.payment.application.port.out.PaymentRepository;
import minjun.ddd.payment.domain.Payment;
import minjun.ddd.payment.domain.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderUsecaseTest {

  @Autowired
  OrderUsecase orderUsecase;

  @Autowired
  DeliveryUsecase deliveryUsecase;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  PaymentRepository paymentRepository;

  @Test
  @DisplayName(value = "10,000원 이상 주문시 Order와 Payment가 생성된다.")
  void placeOrder() {
    // Order 생성, Order.paymentId, Order.state=PAYMENT_APPROVED
    // Payment 생성, Payment.authorizeCode

    final PlaceOrderCommand command = createPlaceOrderCommand(true);

    final Long orderId = orderUsecase.placeOrder(command);

    final Order order = orderRepository.findById(orderId).get();
    final Payment payment = paymentRepository.findById(order.getPaymentId()).get();

    assertThat(order.getPaymentId()).isEqualTo(payment.getId());
    assertThat(order.getState()).isInstanceOf(PaymentApprovedState.class);
    assertThat(payment.getAuthorizedNo()).isNotNull();
  }

  @Test
  @DisplayName(value = "10,000원 미만 주문은 할 수 없다.")
  void placeOrder_fail() {
    // 최소 주문 금액 불변식 검증

    final PlaceOrderCommand command = createPlaceOrderCommand(false);

    assertThatThrownBy(() -> orderUsecase.placeOrder(command))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName(value = "OrderDto를 조회한다.")
  void getOrder() {
    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);

    final OrderDto orderDto = orderUsecase.getOrder(orderId);

    final PaymentInfo paymentInfo = orderDto.getPayment();

    assertThat(paymentInfo.getPaymentId()).isNotNull();
    assertThat(orderDto.getStatus()).isEqualTo(PaymentApprovedState.class.getSimpleName());
    assertThat(paymentInfo.getAuthorizedNo()).isNotNull();
  }

  @Test
  @DisplayName(value = "Order 상태가 배송 전이면 배송지를 변경할 수 있다.")
  void changeOrder() {
    // Delivery 정보 변경

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);
    final DeliveryInfo before = orderUsecase.getOrder(orderId).getDelivery();

    orderUsecase.changeOrder(orderId, createChangeOrderCommand());

    final DeliveryInfo after = orderUsecase.getOrder(orderId).getDelivery();

    assertThat(before.getDeliveryId()).isEqualTo(after.getDeliveryId());
    assertThat(before.getAddress().getAddress()).isNotEqualTo(after.getAddress().getAddress());
    assertThat(before.getAddress().getZipCode()).isNotEqualTo(after.getAddress().getZipCode());
  }

  @Test
  @DisplayName(value = "Order 상태가 배송 중이면 배송지를 변경할 수 없다.")
  void changeOrder_fail() {
    // 배송 정보 관련 불변식 검증

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);
    final DeliveryInfo before = orderUsecase.getOrder(orderId).getDelivery();
    deliveryUsecase.startDelivery(before.getDeliveryId());

    assertThatThrownBy(() -> orderUsecase.changeOrder(orderId, createChangeOrderCommand()))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName(value = "Order가 취소되면 Payment도 취소되어야 한다.")
  void cancelOrder() {
    // Order.state=CANCELLED
    // Payment.status=CANCELLED

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);

    orderUsecase.cancelOrder(orderId);

    final OrderDto orderDto = orderUsecase.getOrder(orderId);

    assertThat(orderDto.getStatus()).isEqualTo(CancelledState.class.getSimpleName());
    assertThat(orderDto.getPayment().getStatus()).isEqualTo(PaymentStatus.CANCELLED.name());
  }

  @Test
  @DisplayName(value = "이미 배송 중이면 Order를 취소할 수 없다.")
  void cancelOrder_whenStateIsNotAcceptable_thenExpectException() {
    // 취소 관련 불변식 검증

    final PlaceOrderCommand command = createPlaceOrderCommand(true);
    final Long orderId = orderUsecase.placeOrder(command);
    final DeliveryInfo orderDto = orderUsecase.getOrder(orderId).getDelivery();
    deliveryUsecase.startDelivery(orderDto.getDeliveryId());

    assertThatThrownBy(() -> orderUsecase.cancelOrder(orderId))
        .isInstanceOf(RuntimeException.class);
  }

  ChangeOrderCommand createChangeOrderCommand() {
    return new ChangeOrderCommand(createDeliveryInfo(2));
  }

  PlaceOrderCommand createPlaceOrderCommand(Boolean isOverMinumum) {
    return PlaceOrderCommand.builder()
        .orderItems(createOrderItems(isOverMinumum))
        .deliveryInfo(createDeliveryInfo(1))
        .paymentInfo(createPaymentInfo())
        .build();
  }

  PaymentInfo createPaymentInfo() {
    return PaymentInfo.builder()
        .cardNo("0000")
        .build();
  }

  DeliveryInfo createDeliveryInfo(int i) {
    return DeliveryInfo.builder()
        .address(Address.builder()
            .zipCode("대한민국 어딘가 우편번호" + i)
            .address("대한민국 어딘가 주소" + i)
            .build()
        )
        .phoneNumber("010-0000-0000")
        .build();
  }

  Set<OrderItem> createOrderItems(Boolean isOverMinimum) {
    if (isOverMinimum) {
      return Set.of(createOrderItem(1L, new BigDecimal(10000L), 1));
    }
    return Set.of(
        createOrderItem(1L, new BigDecimal(1000), 9),
        createOrderItem(1L, new BigDecimal(999), 1)
    );
  }

  OrderItem createOrderItem(Long id, BigDecimal price, Integer quantity) {
    return OrderItem.builder()
        .productId(id)
        .price(price)
        .quantity(quantity)
        .build();
  }
}