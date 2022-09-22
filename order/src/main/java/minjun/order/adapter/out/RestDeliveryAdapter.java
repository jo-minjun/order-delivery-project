package minjun.order.adapter.out;

import minjun.delivery.application.port.in.DeliveryDto;
import minjun.order.application.port.Address;
import minjun.order.application.port.DeliveryInfo;
import minjun.order.application.port.out.ChangeDeliveryRequest;
import minjun.order.application.port.out.CreateDeliveryRequest;
import minjun.order.application.port.out.DeliveryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestDeliveryAdapter implements DeliveryPort {

  private final RestTemplate restTemplate = new RestTemplate();
  private static final String DELIVERY_URL = "localhost:8083/api/delivery";

  @Override
  public Long createDelivery(Long orderId, String zipCode, String address, String phoneNumber) {
    final CreateDeliveryRequest request = new CreateDeliveryRequest(orderId, zipCode, address,
        phoneNumber);

    return restTemplate.postForEntity(DELIVERY_URL, request, Long.class)
        .getBody();
  }

  @Override
  public Boolean changeDeliveryInfo(Long deliveryId, String zipCode, String address,
      String phoneNumber) {

    return restTemplate.patchForObject(
        DELIVERY_URL + "/" + deliveryId + "/change-info",
        new ChangeDeliveryRequest(zipCode, address, phoneNumber),
        Boolean.class);
  }

  @Override
  public DeliveryInfo getDelivery(Long deliveryId) {
    final DeliveryDto deliveryDto = restTemplate.getForObject(DELIVERY_URL + "/" + deliveryId,
        DeliveryDto.class);
    final Address address = Address.builder()
        .address(deliveryDto.getAddress())
        .zipCode(deliveryDto.getZipCode())
        .build();

    return DeliveryInfo.builder()
        .deliveryId(deliveryDto.getId())
        .address(address)
        .phoneNumber(deliveryDto.getPhoneNumber())
        .build();
  }
}
