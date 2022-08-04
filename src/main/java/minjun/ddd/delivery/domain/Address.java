package minjun.ddd.delivery.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"zipCode", "address"})
@ToString
public class Address {

  private String zipCode;
  private String address;
}
