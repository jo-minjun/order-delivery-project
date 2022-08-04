package minjun.ddd.payment.application.port.in;

public interface PaymentUsecase {

  Long createPayment(CreatePaymentCommand command);
  Boolean cancelPayment(Long paymentId);
  PaymentDto getPayment(Long paymentId);
}

