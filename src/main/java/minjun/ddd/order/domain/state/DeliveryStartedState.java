package minjun.ddd.order.domain.state;

public class DeliveryStartedState implements OrderState {

  @Override
  public Integer value() {
    return 30;
  }
}
