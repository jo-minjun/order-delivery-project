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

  private Set<OrderProduct> orderProducts = new HashSet<>();
  private String phoneNumber;
  private String Address;
  private String zipCode;
  private String cardNo;

  @Getter
  @Setter
  @ToString
  public static class OrderProduct {

    private Long productId;
    private BigDecimal price;
    private int quantity;
  }


}
