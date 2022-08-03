package minjun.ddd.order.application;

import lombok.RequiredArgsConstructor;
import minjun.ddd.order.application.port.CreateOrderRequest;
import minjun.ddd.order.domain.DeliveryInfo;
import minjun.ddd.order.domain.Order;
import minjun.ddd.order.domain.OrderLine;
import minjun.ddd.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderAppService {

  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(CreateOrderRequest requestDto) {
    final OrderLine orderLine = OrderMapper.toOrderLine(requestDto.getOrderProducts());
    final DeliveryInfo deliveryInfo = OrderMapper.toDeliveryInfo(
        requestDto.getAddress(), requestDto.getZipCode(), requestDto.getPhoneNumber());

    final Order order = Order.createOrder(orderLine, requestDto.getCardNo(), deliveryInfo);

    orderRepository.save(order);
  }

  @Transactional
  public void cancelOrder(Long orderId) {
    final Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Not Found OrderId: " + orderId));
    order.cancelOrder();
  }
}
