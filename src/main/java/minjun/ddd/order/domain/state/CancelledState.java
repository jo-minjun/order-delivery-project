package minjun.ddd.order.domain.state;

public class CancelledState implements OrderState {

  @Override
  public Integer value() {
    return 99;
  }
}
