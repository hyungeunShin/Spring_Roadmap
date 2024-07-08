package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {
    /*
    @Aspect 를 포함한 org.aspectj 패키지 관련 기능은 aspectjweaver.jar 라이브러리가 제공하는 기능이다.
    spring-boot-starter-aop 를 포함하면 스프링의 AOP 관련 기능과 함께 aspectjweaver.jar 도 함께 사용할 수 있게 의존 관계에 포함된다.
    그런데 스프링에서는 AspectJ가 제공하는 애노테이션이나 관련 인터페이스만 사용하는 것이고, 실제 AspectJ 가 제공하는 컴파일, 로드타임 위버 등을 사용하는 것은 아니다.
    */

    @Around("execution(* hello.aop.order..*(..))")  //@Around 애노테이션의 값인 execution(* hello.aop.order..*(..)) 는 포인트컷
    public Object doLog(ProceedingJoinPoint jp) throws Throwable {
        //@Around 애노테이션의 메서드인 doLog 는 어드바이스(Advice)
        log.info("[log] {}", jp.getSignature());
        return jp.proceed();
    }
}
