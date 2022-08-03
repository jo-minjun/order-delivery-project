package minjun.ddd.common.domain.event.delivery;

import org.springframework.context.ApplicationEvent;

public class DeliveryEvent extends ApplicationEvent {

  public DeliveryEvent(Object source) {
    super(source);
  }
}
