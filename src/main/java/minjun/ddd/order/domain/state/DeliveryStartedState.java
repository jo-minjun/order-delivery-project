package minjun.ddd.order.domain.state;

import minjun.ddd.order.domain.Order;

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
