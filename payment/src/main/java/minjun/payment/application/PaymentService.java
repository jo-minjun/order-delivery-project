package minjun.payment.application;

import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import minjun.payment.application.port.in.CreatePaymentCommand;
import minjun.payment.application.port.in.PaymentDto;
import minjun.payment.application.port.in.PaymentUsecase;
import minjun.payment.application.port.out.PaymentCancelledEvent;
import minjun.payment.application.port.out.PaymentEventPublisher;
import minjun.payment.application.port.out.PaymentPort;
import minjun.payment.application.port.out.PaymentRepository;
import minjun.payment.domain.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService implements PaymentUsecase {

  private final PaymentRepository paymentRepository;
  private final PaymentPort paymentPort;
  private final PaymentEventPublisher paymentEventPublisher;

  @Override
  public PaymentDto createPayment(CreatePaymentCommand command) {
    final Payment payment = Payment.createPayment(command.getOrderId(), command.getCardNo());

    final Optional<String> result = paymentPort.execute(command.getCardNo(), command.getAmount());
    if (result.isPresent()) {
      payment.approvePayment(result.get());
    } else {
      payment.rejectPayment();
    }

    paymentRepository.save(payment);

    return toDto(payment);
  }

  @Override
  public PaymentDto getPayment(Long orderId) {
    final Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(NoSuchElementException::new);

    return toDto(payment);
  }

  @Override
  public Boolean cancelPayment(Long orderId) {
    final Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(NoSuchElementException::new);

    final Boolean responseFromPaymentPort = paymentPort.cancel(payment.getAuthorizedNo());
    if (!responseFromPaymentPort) {
      return false;
    }

    payment.cancelPayment();
    paymentEventPublisher.publish(new PaymentCancelledEvent(payment));

    return true;
  }

  private PaymentDto toDto(Payment payment) {
    return PaymentDto.builder()
        .id(payment.getId())
        .cardNo(payment.getCardNo())
        .status(payment.getStatus().name())
        .authorizedNo(payment.getAuthorizedNo())
        .orderId(payment.getOrderId())
        .build();
  }
}
