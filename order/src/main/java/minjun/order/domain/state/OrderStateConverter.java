package minjun.order.domain.state;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class OrderStateConverter implements AttributeConverter<OrderState, Integer> {

  @Override
  public Integer convertToDatabaseColumn(OrderState attribute) {
    return attribute.value();
  }

  @Override
  public OrderState convertToEntityAttribute(Integer dbData) {
    switch (dbData) {
      case 10:
        return new PlacedState();
      case 20:
        return new PaymentApprovedState();
      case 30:
        return new DeliveryStartedState();
      case 40:
        return new DeliveryCompletedState();
      case 99:
        return new CancelledState();
      default:
        throw new IllegalArgumentException();
    }
  }
}
