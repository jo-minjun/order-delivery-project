package minjun.ddd.order.domain.state;

import minjun.ddd.order.domain.Order;

public interface OrderState {

  default Integer value() {
    return 0;
  }

  default Boolean canChangeDeliveryInfo() {
    return false;
  }

  default void placeOrder(Order context) {
    throw new IllegalStateException();
  }

  default void paymentApproved(Order context) {
    throw new IllegalStateException();
  }

  default void deliveryStarted(Order context) {
    throw new IllegalStateException();
  }

  default void deliveryCompleted(Order context) {
    throw new IllegalStateException();
  }

  default void orderCanceled(Order context) {
    throw new IllegalStateException();
  }
}
