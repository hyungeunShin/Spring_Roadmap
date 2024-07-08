package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {
    /*
    어드바이스는 기본적으로 순서를 보장하지 않는다.
    순서를 지정하고 싶으면 @Aspect 적용 단위로 org.springframework.core.annotation.@Order 애노테이션을 적용해야 한다.
    문제는 이것을 어드바이스 단위가 아니라 클래스 단위로 적용할 수 있다는 점이다.
    하나의 애스펙트에 여러 어드바이스가 있으면 순서를 보장 받을 수 없다.
    따라서 애스펙트를 별도의 클래스로 분리해야 한다.
    */

    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint jp) throws Throwable {
            log.info("[log] {}", jp.getSignature());
            return jp.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect {
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint jp) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", jp.getSignature());
                Object result = jp.proceed();
                log.info("[트랜잭션 커밋] {}", jp.getSignature());
                return result;
            } catch(Exception e) {
                log.info("[트랜잭션 롤백] {}", jp.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", jp.getSignature());
            }
        }
    }
}
