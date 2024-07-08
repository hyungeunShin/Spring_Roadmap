package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {
    /*
    스프링은 프록시 방식의 AOP 를 사용한다.
    따라서 AOP 를 적용하려면 항상 프록시를 통해서 대상 객체(Target)을 호출해야 한다.
    이렇게 해야 프록시에서 먼저 어드바이스를 호출하고, 이후에 대상 객체를 호출한다.
    만약 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 AOP 가 적용되지 않고, 어드바이스도 호출되지 않는다.
    AOP 를 적용하면 스프링은 대상 객체 대신에 프록시를 스프링 빈으로 등록한다.
    따라서 스프링은 의존관계 주입시에 항상 프록시 객체를 주입한다.
    프록시 객체가 주입되기 때문에 대상 객체를 직접 호출하는 문제는 일반적으로 발생하지 않는다.
    하지만 대상 객체의 내부에서 메서드 호출이 발생하면 프록시를 거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다.

    참고
        실제 코드에 AOP 를 직접 적용하는 AspectJ를 사용하면 이런 문제가 발생하지 않는다.
        프록시를 통하는 것이 아니라 해당 코드에 직접 AOP 적용 코드가 붙어 있기 때문에 내부 호출과 무관하게 AOP 를 적용할 수 있다.
        하지만 로드 타임 위빙 등을 사용해야 하는데, 설정이 복잡하고 JVM 옵션을 주어야 하는 부담이 있다.
        그리고 지금부터 설명할 프록시 방식의 AOP 에서 내부 호출에 대응할 수 있는 대안들도 있다.
        이런 이유로 AspectJ를 직접 사용하는 방법은 실무에서는 거의 사용하지 않는다.
        스프링 애플리케이션과 함께 직접 AspectJ 사용하는 방법은 스프링 공식 메뉴얼을 참고하자.
        https://docs.spring.io/spring-framework/reference/core/aop/using-aspectj.html
    */

    @Autowired
    CallServiceV0 service;

    @Test
    void external() {
        /*
        service.external() 을 실행할 때는 프록시를 호출한다.
        따라서 CallLogAspect 어드바이스가 호출된 것을 확인할 수 있다.
        그리고 AOP Proxy 는 target.external() 을 호출한다.

        그런데 여기서 문제는 callServiceV0.external() 안에서 internal() 을 호출할 때 발생한다.
        이때는 CallLogAspect 어드바이스가 호출되지 않는다.
        자바 언어에서 메서드 앞에 별도의 참조가 없으면 this 라는 뜻으로 자기 자신의 인스턴스를 가리킨다.
        결과적으로 자기 자신의 내부 메서드를 호출하는 this.internal() 이 되는데, 여기서 this 는 실제 대상 객체(target)의 인스턴스를 뜻한다.
        결과적으로 이러한 내부 호출은 프록시를 거치지 않는다. 따라서 어드바이스도 적용할 수 없다.
        */
        service.external();
    }

    @Test
    void internal() {
        service.internal();
    }
}