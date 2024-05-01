package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {
    /*
    요구사항
        (완료)- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
        (완료)- 애플리케이션의 흐름을 변경하면 안됨
            (완료)- 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
        (완료)- 메서드 호출에 걸린 시간
        (완료)- 정상 흐름과 예외 흐름 구분
            (완료)- 예외 발생시 예외 정보가 남아야 함
        - 메서드 호출의 깊이 표현
        - HTTP 요청을 구분
            - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
            - 트랜잭션 ID(DB 트랜잭션 X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

    남은 기능은 직전 로그의 깊이와 트랜잭션 ID가 무엇인지 알아야 할 수 있는 일이다.
    예를 들어서 OrderController.request() 에서 로그를 남길 때 사용한 깊이와 트랜잭션 ID를 알아야
    그 다음 로그를 남기는 OrderService.orderItem() 에서 깊이를 표현하고 동일한 트랜잭션 ID를 사용할 수 있다.
    결국 현재 로그의 상태 정보인 트랜잭션 ID 와 level 이 다음으로 전달되어야 한다.
    */

    private final OrderServiceV1 service;

    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV1.request");
            service.orderItem(itemId);
            trace.end(status);
            return "OK";
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
