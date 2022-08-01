package minjun.ddd.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"zipCode", "address"})
@ToString(of = {"zipCode", "address"})
public class Address {

  @Column(length = 6)
  private String zipCode;

  private String address;
}
