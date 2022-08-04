# order-delivery-project

DDD와 Hexagonal architecture를 적용하여 프로젝트 개발

| ACTOR   | SYSTEM   | USECASE          | API CALL                                          | MESSAGING EVENT        | MESSAGE 소비자 |
|---------|----------|------------------|---------------------------------------------------|------------------------|-------------|
| 주문자     | Order    | placeOrder       | Payment.createPayment</br>Delivery.createDelivery |                        |             |
| 주문자     | Order    | getOrder         | Payment.getPayment</br>Delivery.getDelivery       |                        |             |
| 주문자     | Order    | changeOrder      | Delivery.changeDeliveryInfo                       |                        |             |
| 주문자     | Order    | cancelOrder      | Payment.cancelPayment                             |                        |             |
| 기사 (업체) | Delivery | startDelivery    |                                                   | DeliveryStartedEvent   | Order       |
| 기사 (업체) | Delivery | completeDelivery |                                                   | DeliveryCompletedEvent | Order       |
