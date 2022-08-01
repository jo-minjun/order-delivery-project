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
@EqualsAndHashCode(of = {"value"})
@ToString(of = {"value"})
public class Money {

    @Column(name = "money", nullable = false)
    private Integer value;

    public Money(Integer value) {
        this.value = value;
    }

    public Money multiple(int operand) {
        return new Money(value * operand);
    }

    public Money sum(Money money) {
        return new Money(this.value + money.getValue());
    }
}
