package minjun.order.domain.state;

import minjun.order.domain.Order;

public class DeliveryStartedState implements OrderState {

  @Override
  public Integer value() {
    return 30;
  }

  @Override
  public void deliveryCompleted(Order context) {
    context.changeState(new DeliveryCompletedState());
  }
}
