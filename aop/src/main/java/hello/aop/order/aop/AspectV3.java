package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {
    /*
    트랜잭션 기능은 보통 다음과 같이 동작한다.
        - 핵심 로직 실행 직전에 트랜잭션을 시작
        - 핵심 로직 실행
        - 핵심 로직 실행에 문제가 없으면 커밋
        - 핵심 로직 실행에 예외가 발생하면 롤백

    orderService : doLog(), doTransaction() 어드바이스 적용
    orderRepository : doLog() 어드바이스 적용
    */

    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {}

    //클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {}

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint jp) throws Throwable {
        log.info("[log] {}", jp.getSignature());
        return jp.proceed();
    }

    //hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service
    @Around("allOrder() && allService()")
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
