package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {
    /*
    요구사항
        (완료)- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
        (완료)- 애플리케이션의 흐름을 변경하면 안됨
            (완료)- 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
        (완료)- 메서드 호출에 걸린 시간
        (완료)- 정상 흐름과 예외 흐름 구분
            (완료)- 예외 발생시 예외 정보가 남아야 함
        (완료)- 메서드 호출의 깊이 표현
        (완료)- HTTP 요청을 구분
            (완료)- HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
            (완료)- 트랜잭션 ID(DB 트랜잭션 X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

    남은 문제
        - HTTP 요청을 구분하고 깊이를 표현하기 위해서 TraceId 동기화가 필요하다.
            - TraceId 의 동기화를 위해서 관련 메서드의 모든 파라미터를 수정해야 한다.
            - 만약 인터페이스가 있다면 인터페이스까지 모두 고쳐야 하는 상황이다.
        - 로그를 처음 시작할 때는 begin() 을 호출하고 처음이 아닐 때는 beginSync() 를 호출해야 한다.
            - 만약에 컨트롤러를 통해서 서비스를 호출하는 것이 아니라 다른 곳에서 서비스를 처음으로 호출하는 상황이라면 파리미터로 넘길 TraceId 가 없다.
    */

    private final OrderServiceV2 service;

    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderControllerV2.request");
            service.orderItem(status.getTraceId(), itemId);
            trace.end(status);
            return "OK";
        } catch(Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
