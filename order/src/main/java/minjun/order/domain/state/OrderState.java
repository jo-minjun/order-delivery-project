package minjun.order.domain.state;

import java.io.Serializable;
import minjun.order.domain.Order;

public interface OrderState extends Serializable {

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
