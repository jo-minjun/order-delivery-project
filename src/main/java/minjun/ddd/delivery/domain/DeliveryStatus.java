package minjun.ddd.delivery.domain;

public enum DeliveryStatus {

  SUBMITTED, STARTED, COMPLETED;

  public Boolean canChangeDelivery() {
    return this == SUBMITTED;
  }
}

