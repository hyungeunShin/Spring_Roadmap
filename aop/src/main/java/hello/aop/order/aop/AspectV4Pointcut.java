package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV4Pointcut {
    /*
    포인트컷을 공용으로 사용하기 위해 별도의 외부 클래스에 모아두어도 된다.
    외부에서 호출할 때는 포인트컷의 접근 제어자를 public 으로 열어두어야 한다.
    */

    @Around("hello.aop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint jp) throws Throwable {
        log.info("[log] {}", jp.getSignature());
        return jp.proceed();
    }

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
