package minjun.ddd.order.application.port.out;

import minjun.ddd.common.domain.event.order.OrderEvent;

/**
 * @Deprecated
 * 도메인 애그리거트에 AbstractAggregateRoot<AggregateClass>를 상속하면
 * 도메인 레이어에서 도메인 이벤트를 발행할 수 있다.
 * this.registerEvent(new Event(AggregateInstance))
 */
@Deprecated
public interface OrderEventPublisher {

  void publish(OrderEvent event);

}


