package minjun.ddd.order.application.port;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateOrderRequest {

  private Set<OrderProductRequest> orderProducts = new HashSet<>();
  private String cardNo;
  private DeliveryInfoRequest deliveryInfo;

  @Getter
  @Setter
  @ToString
  public static class OrderProductRequest {

    private Long productId;
    private BigDecimal price;
    private int quantity;
  }

  @Getter
  @Setter
  @ToString
  public static class DeliveryInfoRequest {

    private String Address;
    private String zipCode;
    private String phoneNumber;
  }
}
