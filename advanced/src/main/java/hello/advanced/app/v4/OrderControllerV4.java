package hello.advanced.app.v4;

import hello.advanced.trace.logtrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {
    /*
    로그를 남기는 부분을 모아서 하나로 모듈화하고 비즈니스 로직 부분을 분리했다.
    만약 로그를 남기는 로직을 변경해야 한다면 단순히 AbstractTemplate 코드만 변경하면 된다.
    템플릿이 없는 V3 상태에서 로그를 남기는 로직을 변경해야 한다면 모든 클래스를 다 찾아서 고쳐야 한다.

    ※ 단점
    상속을 받는 다는 것은 특정 부모 클래스를 의존하고 있다는 것이다.
    자식 클래스 입장에서는 부모 클래스의 기능을 사용하든 사용하지 않든 간에 부모 클래스를 알아야한다.
    이런 의존관계 때문에 부모 클래스를 수정하면 자식 클래스에도 영향을 줄 수 있다.
    추가로 템플릿 메서드 패턴은 상속 구조를 사용하기 때문에 별도의 클래스나 익명 내부 클래스를 만들어야 하는 부분도 복잡하다.

    이런 부분들을 더 깔끔하게 개선하려면 어떻게 해야할까?
    템플릿 메서드 패턴과 비슷한 역할을 하면서 상속의 단점을 제거할 수 있는 디자인 패턴이 바로 전략 패턴(Strategy Pattern)이다.
    */

    private final OrderServiceV4 service;

    private final LogTrace trace;

    @GetMapping("/v4/request")
    public String request(String itemId) {
        AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
            @Override
            protected String call() {
                service.orderItem(itemId);
                return "OK";
            }
        };
        return template.execute("OrderControllerV4.request");
    }
}
