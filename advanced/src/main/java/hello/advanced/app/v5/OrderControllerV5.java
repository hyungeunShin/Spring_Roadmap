package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logtrace.LogTrace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {
    /*
    지금까지 변하는 코드와 변하지 않는 코드를 분리하고 더 적은 코드로 로그 추적기를 적용했다.
    템플릿 메서드 패턴, 전략 패턴, 템플릿 콜백 패턴까지 진행하면서 변하는 코드와 변하지 않는 코드를 분리했다.
    그리고 최종적으로 템플릿 콜백 패턴을 적용하고 콜백으로 람다를 사용해서 코드 사용도 최소화 할 수 있었다.

    한계
    그런데 지금까지 설명한 방식의 한계는 아무리 최적화를 해도 결국 로그 추적기를 적용하기 위해서 원본 코드를 수정해야 한다는 점이다.
    클래스가 수백개이면 수백개를 더 힘들게 수정하는가 조금 덜 힘들게 수정하는가의 차이가 있을 뿐 본질적으로 코드를 다 수정해야 하는 것은 마찬가지이다.
    */

    private final OrderServiceV5 service;

    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 service, LogTrace trace) {
        this.service = service;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId) {
        return template.execute("OrderControllerV5.request", () -> {
            service.orderItem(itemId);
            return "OK";
        });
    }
}
