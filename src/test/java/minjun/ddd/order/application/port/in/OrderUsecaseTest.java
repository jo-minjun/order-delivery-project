package minjun.ddd.order.application.port.in;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderUsecaseTest {

  @Test
  void placeOrder() {
    // Order 생성, Order.paymentId, Order.state=PAYMENT_APPROVED
    // Payment 생성, Payment.authorizeCode
  }

  @Test
  void placeOrder_whenMinimumAmountIsNotSatisfied_thenExpectException() {
    // 최소 주문 금액 불변식 검증
  }

  @Test
  void changeOrder() {
    // 배송 정보 관련 불변식 검증
    // Delivery 정보 변경
  }

  @Test
  void changeOrder_whenStateIsNotAcceptable_thenExpectException() {
    // 배송 정보 관련 불변식 검증
  }

  @Test
  void cancelOrder() {
    // 취소 관련 불변식 검증
    // Order.state=CANCELLED
    // Payment.status=CANCELLED
  }

  @Test
  void cancelOrder_whenStateIsNotAcceptable_thenExpectException() {
    // 취소 관련 불변식 검증
  }
}