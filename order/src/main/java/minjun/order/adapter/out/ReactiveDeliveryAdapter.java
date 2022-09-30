package minjun.order.adapter.out;

import minjun.delivery.application.port.in.DeliveryDto;
import minjun.order.application.port.Address;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.out.ChangeDeliveryRequest;
import minjun.order.application.port.out.CreateDeliveryRequest;
import minjun.order.application.port.out.DeliveryReactivePort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReactiveDeliveryAdapter implements DeliveryReactivePort {

  private final WebClient webClient = WebClient.create();
  private static final String DELIVERY_URL = "localhost:8083/api/delivery";

  @Override
  public Mono<Long> createDelivery(
      Long orderId,
      String zipCode,
      String address,
      String phoneNumber
  ) {
    return webClient.post()
        .uri(DELIVERY_URL)
        .bodyValue(new CreateDeliveryRequest(orderId, zipCode, address, phoneNumber))
        .retrieve()
        .bodyToMono(Long.class);
  }

  @Override
  public Mono<Boolean> changeDeliveryInfo(
      Long orderId,
      String zipCode,
      String address,
      String phoneNumber
  ) {
    return webClient.patch()
        .uri(DELIVERY_URL + "/orders/" + orderId + "/change-info")
        .bodyValue(new ChangeDeliveryRequest(zipCode, address, phoneNumber))
        .retrieve()
        .bodyToMono(Boolean.class);
  }

  @Override
  public Mono<DeliveryInfo> getDelivery(Long orderId) {
    return webClient.get()
        .uri(DELIVERY_URL + "/orders/" + orderId)
        .retrieve()
        .bodyToMono(DeliveryDto.class)
        .map(deliveryDto -> {
          final Address address = Address.builder()
              .address(deliveryDto.getAddress())
              .zipCode(deliveryDto.getZipCode())
              .build();

          return DeliveryInfo.builder()
              .deliveryId(deliveryDto.getId())
              .address(address)
              .phoneNumber(deliveryDto.getPhoneNumber())
              .build();
        });
  }
}
