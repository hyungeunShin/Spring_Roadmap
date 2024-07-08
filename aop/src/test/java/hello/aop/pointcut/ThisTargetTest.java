package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/*
application.properties
spring.aop.proxy-target-class=true  --> CGLIB
spring.aop.proxy-target-class=false --> JDK 동적 프록시

@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})
    application.properties 에 설정하는 대신에 해당 테스트에서만 설정을 임시로 적용한다.
    이렇게 하면 각 테스트마다 다른 설정을 손쉽게 적용할 수 있다.
*/
@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false")   //JDK
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")    //CGLIB(기본값)
class ThisTargetTest {
    /*
    this : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
    target : Target 객체(스프링 AOP 프록시가 가리키는 실제 대상)를 대상으로 하는 조인 포인트
        - this, target 은 다음과 같이 적용 타입 하나를 정확하게 지정해야 한다.
        - * 같은 패턴을 사용할 수 없다.
        - 부모 타입을 허용한다.

    this vs target
        - this 는 스프링 빈으로 등록되어 있는 프록시 객체를 대상으로 포인트컷을 매칭한다.
        - target 은 실제 target 객체를 대상으로 포인트컷을 매칭한다.

    프록시 생성 방식에 따른 차이
        스프링은 프록시를 생성할 때 JDK 동적 프록시와 CGLIB 를 선택할 수 있다. 둘의 프록시를 생성하는 방식이 다르기 때문에 차이가 발생한다.
        - JDK 동적 프록시: 인터페이스가 필수이고, 인터페이스를 구현한 프록시 객체를 생성한다.
        - CGLIB: 인터페이스가 있어도 구체 클래스를 상속 받아서 프록시 객체를 생성한다.

    this, target 지시자는 단독으로 사용되기 보다는 파라미터 바인딩에서 주로 사용
    */

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService proxy : {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint jp) throws Throwable {
            log.info("[this-interface] {}", jp.getSignature());
            return jp.proceed();
        }

        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint jp) throws Throwable {
            log.info("[target-interface] {}", jp.getSignature());
            return jp.proceed();
        }

        /*
        this: 스프링 AOP 프록시 객체 대상
        JDK 동적 프록시는 인터페이스를 기반으로 생성되므로 구현 클래스를 알 수 없음
        CGLIB 프록시는 구현 클래스를 기반으로 생성되므로 구현 클래스를 알 수 있음
        */
        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint jp) throws Throwable {
            log.info("[this-impl] {}", jp.getSignature());
            return jp.proceed();
        }

        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint jp) throws Throwable {
            log.info("[target-impl] {}", jp.getSignature());
            return jp.proceed();
        }
    }
}
