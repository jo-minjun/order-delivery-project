package minjun.ddd.order.domain;

public enum OrderStatus {

  PLACED, PAYMENT_APPROVED, DELIVERY_STARTED, DELIVERY_COMPLETED, CANCELED;

  public boolean isNotYetDeliveryStarted() {
    return this.equals(PLACED) || this.equals(PAYMENT_APPROVED);
  }
}
