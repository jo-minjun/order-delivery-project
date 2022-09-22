package minjun.payment.application;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import minjun.payment.application.port.in.CreatePaymentCommand;
import minjun.payment.application.port.in.PaymentDto;
import minjun.payment.application.port.in.PaymentUsecase;
import minjun.payment.application.port.out.PaymentApprovedEvent;
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
  public Long createPayment(CreatePaymentCommand command) {
    final Payment payment = Payment.createPayment(command.getOrderId(), command.getCardNo());

    final String authorizedNo = paymentPort.execute(command.getCardNo(), command.getAmount());

    payment.approvePayment(authorizedNo);
    paymentRepository.save(payment);
    paymentEventPublisher.publish(new PaymentApprovedEvent(payment));

    return payment.getId();
  }

  @Override
  public Boolean cancelPayment(Long paymentId) {
    final Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(NoSuchElementException::new);

    final Boolean responseFromPaymentPort = paymentPort.cancel(payment.getAuthorizedNo());
    if (!responseFromPaymentPort) {
      return false;
    }

    payment.cancelPayment();
    paymentEventPublisher.publish(new PaymentCancelledEvent(payment));

    return true;
  }

  @Override
  public PaymentDto getPayment(Long paymentId) {
    final Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(NoSuchElementException::new);

    return PaymentDto.builder()
        .id(payment.getId())
        .cardNo(payment.getCardNo())
        .status(payment.getStatus().name())
        .authorizedNo(payment.getAuthorizedNo())
        .orderId(payment.getOrderId())
        .build();
  }
}
