package minjun.ddd.payment.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.payment.application.port.in.CreatePaymentCommand;
import minjun.ddd.payment.application.port.in.PaymentUsecase;
import minjun.ddd.payment.application.port.out.*;
import minjun.ddd.payment.domain.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
        .orElseThrow(() -> new NoSuchElementException());

    final Boolean responseFromPaymentPort = paymentPort.cancel(payment.getAuthorizedNo());
    if (!responseFromPaymentPort) {
      return false;
    }

    payment.cancelPayment();
    paymentEventPublisher.publish(new PaymentCancelledEvent(payment));

    return true;
  }
}
