package minjun.order.domain.state;

public class DeliveryCompletedState implements OrderState {

  @Override
  public Integer value() {
    return 40;
  }
}
