package minjun.order.domain.state;

import minjun.order.domain.Order;

public class PlacedState implements OrderState {

  @Override
  public Integer value() {
    return 10;
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
  public void paymentApproved(Order context) {
    context.changeState(new PaymentApprovedState());
  }

  @Override
  public void deliveryStarted(Order context) {
    context.changeState(new DeliveryStartedState());
  }
}
