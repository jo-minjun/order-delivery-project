package minjun.order.domain.state;

import minjun.order.domain.Order;

public class PaymentApprovedState implements OrderState {

  @Override
  public Integer value() {
    return 20;
  }

  @Override
  public Boolean canChangeDeliveryInfo() {
    return true;
  }

  @Override
  public void orderCanceled(Order context) {
    context.changeState(new CancelledState());
  }

  @Override
  public void deliveryStarted(Order context) {
    context.changeState(new DeliveryStartedState());
  }
}
