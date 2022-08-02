package minjun.ddd.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"zipCode", "address"})
@ToString
public class Address {

  @Column(length = 5)
  private String zipCode;

  private String address;

  @Builder
  public Address(String zipCode, String address) {
    this.zipCode = zipCode;
    this.address = address;
  }
}
