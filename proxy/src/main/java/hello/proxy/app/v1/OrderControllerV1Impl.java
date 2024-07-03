package hello.proxy.app.v1;

public class OrderControllerV1Impl implements OrderControllerV1 {
    private final OrderServiceV1 service;

    public OrderControllerV1Impl(OrderServiceV1 service) {
        this.service = service;
    }

    @Override
    public String request(String itemId) {
        service.orderItem(itemId);
        return "OK";
    }

    @Override
    public String noLog() {
        return "OK";
    }
}
