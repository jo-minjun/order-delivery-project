package minjun.ddd.order.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.order.application.port.CreateOrderRequest;
import minjun.ddd.order.application.port.CreateOrderRequest.DeliveryInfoRequest;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderAppService {

  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(CreateOrderRequest requestDto) {
    final Order order = OrderMapper.toOrder(requestDto);

    orderRepository.save(order);
  }

  @Transactional
  public void cancelOrder(Long orderId) {
    final Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Not Found OrderId: " + orderId));
    order.cancelOrder();
    orderRepository.save(order);
  }

  public void changeDeliveryInfo(Long orderId, DeliveryInfoRequest deliveryInfoRequest) {
    final Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Not Found OrderId: " + orderId));
    order.changeDeliveryInfo(OrderMapper.toDeliveryInfo(deliveryInfoRequest));
    orderRepository.save(order);
  }
}
