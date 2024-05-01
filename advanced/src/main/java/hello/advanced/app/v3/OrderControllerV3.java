package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {
    /*
    V0는 해당 메서드가 실제 처리해야 하는 핵심 기능만 깔끔하게 남아있다.
    반면에 V3에는 핵심 기능보다 로그를 출력해야 하는 부가 기능 코드가 훨씬 더 많다.

    핵심 기능 vs 부가 기능
        - 핵심 기능은 해당 객체가 제공하는 고유의 기능이다. 예를 들어서 orderService 의 핵심 기능은 주문 로직이다.
          메서드 단위로 보면 orderService.orderItem() 의 핵심 기능은 주문 데이터를 저장하기 위해 리포지토리를 호출하는 orderRepository.save(itemId) 코드가 핵심 기능이다.
        - 부가 기능은 핵심 기능을 보조하기 위해 제공되는 기능이다. 예를 들어서 로그 추적 로직, 트랜잭션 기능이 있다.
          이러한 부가 기능은 단독으로 사용되지는 않고 핵심 기능과 함께 사용된다.
          예를 들어서 로그 추적 기능은 어떤 핵심 기능이 호출되었는지 로그를 남기기 위해 사용한다.
          그러니까 핵심 기능을 보조하기 위해 존재한다.

    Controller, Service, Repository 의 코드를 보면 로그 추적기를 사용하는 구조는 모두 동일하다. 중간에 핵심 기능을 사용하는 코드만 다를 뿐이다.

    ※ 변하는 것과 변하지 않는 것을 분리
    좋은 설계는 변하는 것과 변하지 않는 것을 분리하는 것이다. 여기서 핵심 기능 부분은 변하고, 로그 추적기를 사용하는 부분은 변하지 않는 부분이다.
    */

    private final OrderServiceV3 service;

    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV3.request");
            service.orderItem(itemId);
            trace.end(status);
            return "OK";
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
