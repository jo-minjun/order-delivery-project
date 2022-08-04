# order-delivery-project

DDD와 Hexagonal Architecture를 적용하여 프로젝트 개발

## Run

```shell
./gradlew test
```

## Design

### Usecase

| ACTOR   | SYSTEM   | USECASE          | API CALL                                          | MESSAGING EVENT        | MESSAGE 소비자 |
|---------|----------|------------------|---------------------------------------------------|------------------------|-------------|
| 주문자     | Order    | placeOrder       | Payment.createPayment</br>Delivery.createDelivery |                        |             |
| 주문자     | Order    | getOrder         | Payment.getPayment</br>Delivery.getDelivery       |                        |             |
| 주문자     | Order    | changeOrder      | Delivery.changeDeliveryInfo                       |                        |             |
| 주문자     | Order    | cancelOrder      | Payment.cancelPayment                             |                        |             |
| 기사 (업체) | Delivery | startDelivery    |                                                   | DeliveryStartedEvent   | Order       |
| 기사 (업체) | Delivery | completeDelivery |                                                   | DeliveryCompletedEvent | Order       |

### Invariables

- 배송이 시작되면 주문을 취소할 수 없다
- 취소 주문 금액은 10,000원이다
- 배송이 시작되면 배송 정보 변경 불가능하다

### Class

![](https://plantuml-server.kkeisuke.dev/svg/bLPFJnD15B_lfvYR6cnIyBOX3QNKqAIb3QtSXzsXJDpTMMTdcm8XgOaXH3pWeP6e68oQIEA1Y54E-eNOvJluzczdbwrKBioyztxlp--zDzibHs0kNDDGb7j8trJHwW8Sl9-7tkN8Fpf5yohoBYxyWu_-wG_a7xttFxpsZa-ySxXy-krzk_9FNo3_rrV_uDND-0pvXoV8VtFW7NtEE5bGOviOXAwl3lqnk1oX-VB3Shdilpi6_tdOewcn8p7opyRU9TmkHzVV_mJ0wukHz_BBpVWaSbMt11Kx3z4wiPb31UDmxX8-e1f93ptY231T9KFSPnOsK9LhsrGGJRYSG8Jd_jjpqC-XuN28RHkE22vOnxOWFBm5Tsh5HuHAg3uWbbZ5bcv85imLUPEEksbGPpis0gUsRL0D2yeIRwKWwYHxMHgdd_hNcOcpMSJ5Rs33XN8VOoqY80mDTefYO-qfxXDaSwQxce3mQ4zH0hHcOCT1dKYE5XSvOw9I0IrAtLDTHKrczIMHXKsYegxWD2EsEUIfeXQpo8uarecZSMe7rMP0-ugoFycDSPtmMvcr0-dCUOK-cjI29Dh9KL8B9h1HDPbhYQ8i3JeWVATnswsDTqoWia03EoMIODtWd1IMgo181AeO11tAbM60gY68wG0tH2msusibzeIGv2jHJd4LjxUeWkBEF7DnE3igQbY2z64-Cq4f2FaMreXK88GEYsvXM1uhcy5SmbJLXbG6HByVuXweQC2eBfabN7U39-bkinepROFymp3iUmrR6Z6AZ78iTh99HcNH84GWTkHYz_Obt4esw__6fMNCZ8nACkv4X91eLKgLtEXFve-1BfaW6M6GBP70eY6Mzp9fGhIQ0QoWj9at5Eiw9mxCUZKwv7R91Or4CzoB6XXZfsp1BhLhJ2zwdzBWsKSe-4Siryn5pZZjFbbkDNgz-eeiwrNNU_LrMLHhjphDUc8M-qv90fBbiYVok0EvogKcn7jr--6EvBEohs6khx42PXBkOtyIzJ5phjXcdEwIF5ocBmE-axrgfxFUtiY8QjMrMhtPpB8NUqr9ogOeSQGnqsHMbf_mmPio9w5CHSkqlq8qQc93-YYb7wDIz5UHFfq5Icb7Yg1fymgKKTQArE2CN5aYbWu_slu2.svg)

### State Transition

![](https://plantuml-server.kkeisuke.dev/svg/XLHTQuCm57ttL-Ijsw3x0NkOpJGF1Qt1ka8PGuBUXms_o5oXZFttvKCpJMeh299ppiqzvrxSVFMCzzzrvNahL4BrSG9-PbttE7xl9GxbtJrQFwEMby0leWlgeAsx2fRr4rnKwDYvXgQN1F7ozWHKdrZgWOSrnz5o-8IY7wGpeVe8M5D0LQcUVD_tF1513uQ4VZmadhU7TxGMMXhXW8HFgAjO0OdaYEiaGGEQWuynsMKvfZHD3f9jjwyL5arfGn9j3oGzvliCfvdKgec4Gq23r6RDfK4IquWeSJ44kgGtL5L1cBIz17WNa2XIjLHGCy-N75nL672y3tc_O-AJXHb2NqtuSZJJGUnVN-9jfaroO8v5WsksoYbTY7D4TVytqtD3Cnx-ryxUcznfsDg6VDunlRa-yzZDxER7D7VcOPTnPYBjRA0fnM_W3m00.svg)

### Hexagonal Architecture

```shell
└── delivery
    ├── adapter
    │   ├── in
    │   │   ├── xxxController.java
    │   │   └── EventHandler.java (or MessageHandler.java)
    │   └── out
    │       └── EventPublisher.java (or MessagePublisher.java)
    ├── application
    │   ├── xxxService.java
    │   └── port
    │       ├── in
    │       │   ├── xxxCommand.java
    │       │   ├── xxxDto.java
    │       │   └── xxxUsecase.java
    │       └── out
    │           ├── xxxEvent.java
    │           ├── xxxEventPublisher.java
    │           └── xxxRepository.java
    └── domain
        ├── AggregateRootEntity.java
        └── ValueObject.java
```
