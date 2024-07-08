package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
class AtTargetAtWithinTest {
    /*
    @target : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
    @within : 주어진 애노테이션이 있는 타입 내 조인 포인트

    @target vs @within
        - @target 은 인스턴스의 모든 메서드를 조인 포인트로 적용한다.
        - @within 은 해당 타입 내에 있는 메서드만 조인 포인트로 적용한다.
        - @target 은 부모 클래스의 메서드까지 어드바이스를 다 적용하고, @within 은 자기 자신의 클래스에 정의된 메서드에만 어드바이스를 적용

    @target, @within 지시자는 파라미터 바인딩에서 함께 사용

    주의
        args, @args, @target 포인트컷 지시자는 단독으로 사용하면 안된다.
        이번 예제를 보면 execution(* hello.aop..*(..)) 를 통해 적용 대상을 줄여준 것을 확인할 수 있다.
        args, @args, @target 은 실제 객체 인스턴스가 생성되고 실행될 때 어드바이스 적용 여부를 확인할 수 있다.
        실행 시점에 일어나는 포인트컷 적용 여부도 결국 프록시가 있어야 실행 시점에 판단할 수 있다.
        프록시가 없다면 판단 자체가 불가능하다.
        그런데 스프링 컨테이너가 프록시를 생성하는 시점은 스프링 컨테이너가 만들어지는 애플리케이션 로딩 시점에 적용할 수 있다.
        따라서 args , @args , @target 같은 포인트컷 지시자가 있으면 스프링은 모든 스프링 빈에 AOP 를 적용하려고 시도한다.
        앞서 설명한 것 처럼 프록시가 없으면 실행 시점에 판단 자체가 불가능하다.
        문제는 이렇게 모든 스프링 빈에 AOP 프록시를 적용하려고 하면 스프링이 내부에서 사용하는 빈 중에는 final 로 지정된 빈들도 있기 때문에 오류가 발생할 수 있다.
        따라서 이러한 표현식은 최대한 프록시 적용 대상을 축소하는 표현식과 함께 사용해야 한다.

        여기서 말하는 '실행'은 프록시로 생성된 인스턴스의 메소드 실행을 의미한다.
        즉, 애플리케이션의 전체 실행이 아니라, AOP 가 적용된 메소드가 호출될 때를 의미한다.
        프록시는 이 메소드 호출이 포인트컷에 정의된 패턴과 일치하는지를 실행 시점에 검사하여, 조건에 맞는 경우에만 어드바이스(Advice)를 적용한다.
        예를 들어 @target 지시자는 특정 어노테이션이 붙은 대상 객체에만 AOP 를 적용하도록 한다.
        하지만 중요한 점은 스프링이 이런 포인트컷을 사용할 때, 실제 인스턴스가 생성되어 해당 어노테이션이 적용되었는지 확인하기 전에는 정확하게 AOP 적용 여부를 결정할 수 없다는 것이다.
        따라서 스프링은 포인트컷 조건을 충족할 가능성이 있는 모든 빈에 대해 프록시를 생성하고, 실제 메소드 실행 시에 포인트컷 조건이 맞는지를 검사하게 됩니다.
    */

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child proxy : {}", child.getClass());
        child.childMethod();
        child.parentMethod();
    }

    /*
    @SpringBootTest 를 사용하면서 그 안에서
    내부 클래스로 @Configuration 을 사용하게 되면 스프링 부트의 다양한 설정들이 먹히지 않고,
    @Configuration 안에서만 적용한 설정이 먹히게 됩니다.

    이 설정이 우선권을 가지고 가버리기 때문에 스프링 부트의 다른 설정이 적용되지 않습니다. 쉽게 이야기해서 테스트에서는 이 설정만 사용하게 됩니다.
    여기서 문제는 스프링 부트가 만들어주는 AOP 관련 기본 클래스들도 스프링 빈으로 등록되지 않고, 결국 AOP 자체가 동작하지 않게 된다는 점입니다. (AutoProxy 가 있어야 AOP 를 적용할 수 있다.)

    그러면 AOP 가 동작하도록 하는 기능들을 등록하면 되겠지요?
    바로 @EnableAspectJAutoProxy 를 다음과 같이 직접 넣어주시면 됩니다. 그러면 AOP 가 적용되면서 정상 동작하게 됩니다.

    그런데 이렇게 사용하는 것은 많이 번거롭습니다.
    스프링 부트는 스프링 부트의 기본 기능을 설정을 사용하면서 그 위에 원하는 설정을 더하는 방법도 제공합니다.
    바로 @TestConfiguration 을 사용하면 됩니다.
    그러면 스프링 부트 설정을 그대로 다 적용하면서, 그 위에 원하는 클래스만 살짝 올릴 수 있습니다.
    */
    //@Configuration
    //@EnableAspectJAutoProxy

    //@TestConfiguration
    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent {
        public void parentMethod() {}   //부모에만 있는 메서드
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {}
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {
        //@target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint jp) throws Throwable {
            log.info("[@target] {}", jp.getSignature());
            return jp.proceed();
        }

        //@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint jp) throws Throwable {
            log.info("[@within] {}", jp.getSignature());
            return jp.proceed();
        }
    }
}
