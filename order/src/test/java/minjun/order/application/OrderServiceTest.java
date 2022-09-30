package minjun.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import minjun.order.Fixture;
import minjun.order.application.port.out.DeliveryReactivePort;
import minjun.order.application.port.out.OrderEventPublisher;
import minjun.order.application.port.out.OrderRepository;
import minjun.order.application.port.out.PaymentReactivePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(value = SpringExtension.class)
class OrderServiceTest {

  @InjectMocks
  OrderService target;

  @Mock
  OrderEventPublisher publisher;

  @Mock
  OrderRepository orderRepository;

  @Mock
  DeliveryReactivePort deliveryPort;

  @Mock
  PaymentReactivePort paymentPort;

  @Test
  void 주문을_조회한다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));
    when(deliveryPort.getDelivery(any()))
        .thenReturn(Mono.just(Fixture.getDeliveryInfo()));
    when(paymentPort.getPayment(any()))
        .thenReturn(Mono.just(Fixture.getPaymentInfo()));

    StepVerifier.create(target.getOrder(1L))
        .assertNext(orderDto -> {
          assertThat(orderDto.getOrderId())
              .isEqualTo(Fixture.getOrderPlaced().getId());
          assertThat(orderDto.getDelivery().getDeliveryId())
              .isEqualTo(Fixture.getDeliveryInfo().getDeliveryId());
          assertThat(orderDto.getPayment().getPaymentId())
              .isEqualTo(Fixture.getPaymentInfo().getPaymentId());
        })
        .verifyComplete();
  }

  @Test
  void 총_주문_금액이_최소금액_이상이면_주문에_성공한다() {
    when(orderRepository.save(any()))
        .thenReturn(Fixture.getOrderPlaced());
    when(deliveryPort.createDelivery(any(), any(), any(), any()))
        .thenReturn(Mono.just(Fixture.getDeliveryInfo().getDeliveryId()));
    when(paymentPort.createPayment(any(), any(), any()))
        .thenReturn(Mono.just(Fixture.getPaymentInfo()));

    StepVerifier.create(target.placeOrder(Fixture.getPlaceOrderCommand(10_000)))
        .assertNext(orderId -> {
          assertThat(orderId).isEqualTo(Fixture.getOrderPlaced().getId());
          verify(publisher, times(1)).publish(any());
          verify(orderRepository, times(1)).save(any());
          verify(deliveryPort, times(1)).createDelivery(any(), any(), any(), any());
          verify(paymentPort, times(1)).createPayment(any(), any(), any());
        })
        .verifyComplete();
  }

  @Test
  void 총_주문_금액이_최소금액_미만이면_주문에_실패한다() {
    StepVerifier.create(target.placeOrder(Fixture.getPlaceOrderCommand(9_999)))
        .verifyError(RuntimeException.class);
  }

  @Test
  void deliveryPort의_응답이_true이면_주문_배송지_변경에_성공한다() {
    when(deliveryPort.changeDeliveryInfo(any(), any(), any(), any()))
        .thenReturn(Mono.just(true));
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));

    StepVerifier.create(target.changeOrder(1L, Fixture.getChangeOrderCommand()))
        .verifyComplete();
    verify(deliveryPort, times(1)).changeDeliveryInfo(any(), any(), any(), any());
  }

  @Test
  void deliveryPort의_응답이_false이면_주문_배송지_변경에_실패한다() {
    when(deliveryPort.changeDeliveryInfo(any(), any(), any(), any()))
        .thenReturn(Mono.just(false));
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));

    StepVerifier.create(target.changeOrder(1L, Fixture.getChangeOrderCommand()))
        .verifyError(RuntimeException.class);
    verify(deliveryPort, times(1)).changeDeliveryInfo(any(), any(), any(), any());
  }

  @Test
  void 주문됨_상태에서_주문을_취소할_수_있다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));
    when(paymentPort.cancelPayment(any()))
        .thenReturn(Mono.just(true));

    StepVerifier.create(target.cancelOrder(1L))
        .verifyComplete();
    verify(paymentPort, times(1)).cancelPayment(any());
  }

  @Test
  void 결제됨_상태에서_주문을_취소할_수_있다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPaymentApproved()));
    when(paymentPort.cancelPayment(any()))
        .thenReturn(Mono.just(true));

    StepVerifier.create(target.cancelOrder(1L))
        .verifyComplete();
    verify(paymentPort, times(1)).cancelPayment(any());
  }

  @Test
  void 배송_시작됨_상태에서_주문을_취소할_수_없다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderDeliveryStarted()));
    when(paymentPort.cancelPayment(any()))
        .thenReturn(Mono.just(true));

    StepVerifier.create(target.cancelOrder(1L))
        .verifyError(IllegalStateException.class);
    verify(paymentPort, times(1)).cancelPayment(any());
  }

  @Test
  void 배송을_시작한다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));

    StepVerifier.create(target.startDelivery(1L))
        .as("배송 시작 이벤트 발생시 수행됨")
        .verifyComplete();
  }

  @Test
  void 배송_시작_상태에서_배송_완료_상태로_변경할_수_있다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderDeliveryStarted()));

    StepVerifier.create(target.completeDelivery(1L))
        .as("배송 완료 이벤트 발생시 수행됨")
        .verifyComplete();
  }

  @Test
  void 주문됨_상태에서_배송_완료_상태로_변경할_수_없다() {
    when(orderRepository.findById(any()))
        .thenReturn(Optional.of(Fixture.getOrderPlaced()));

    StepVerifier.create(target.completeDelivery(1L))
        .as("배송 완료 이벤트 발생시 수행됨")
        .verifyError(IllegalStateException.class);
  }

  @BeforeEach
  void setUp() {
    doNothing().when(publisher).publish(any());
  }
}