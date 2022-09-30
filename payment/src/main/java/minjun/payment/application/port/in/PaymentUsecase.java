package minjun.payment.application.port.in;

public interface PaymentUsecase {

  PaymentDto createPayment(CreatePaymentCommand command);
  Boolean cancelPayment(Long orderId);
  PaymentDto getPayment(Long orderId);
}

