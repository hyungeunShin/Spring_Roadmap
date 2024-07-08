package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV3Test {
    /*
    AOP 는 주로 트랜잭션 적용이나 주요 컴포넌트의 로그 출력 기능에 사용된다.
    쉽게 이야기해서 인터페이스에 메서드가 나올 정도의 규모에 AOP 를 적용하는 것이 적당하다.
    더 풀어서 이야기하면 AOP 는 public 메서드에만 적용한다.
    private 메서드처럼 작은 단위에는 AOP 를 적용하지 않는다.
    AOP 적용을 위해 private 메서드를 외부 클래스로 변경하고 public 으로 변경하는 일은 거의 없다.
    그러나 위 예제와 같이 public 메서드에서 public 메서드를 내부 호출하는 경우에는 문제가 발생한다.
    */

    @Autowired
    CallServiceV3 service;

    @Test
    void external() {
        service.external();
    }
}