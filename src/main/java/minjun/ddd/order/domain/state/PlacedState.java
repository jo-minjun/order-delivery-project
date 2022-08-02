package minjun.ddd.order.domain.state;

import minjun.ddd.order.domain.Order;

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
}
