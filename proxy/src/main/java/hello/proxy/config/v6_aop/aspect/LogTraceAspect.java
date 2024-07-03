package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {
    /*
    스프링 애플리케이션에 프록시를 적용하려면 포인트컷과 어드바이스로 구성되어 있는 어드바이저(Advisor)를 만들어서 스프링 빈으로 등록하면 된다.
    그러면 나머지는 앞서 배운 자동 프록시 생성기가 모두 자동으로 처리해준다.
    자동프록시 생성기는 스프링 빈으로 등록된 어드바이저들을 찾고, 스프링 빈들에 자동으로 프록시를 적용해준다. (물론 포인트컷이 매칭되는 경우에 프록시를 생성한다.)
    스프링은 @Aspect 애노테이션으로 매우 편리하게 포인트컷과 어드바이스로 구성되어 있는 어드바이저 생성 기능을 지원한다.
    */
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    //@Around 의 메서드는 어드바이스(Advice)가 된다.
    @Around("execution(* hello.proxy.app..*(..))")
    public Object execute(ProceedingJoinPoint jp) throws Throwable {
        /*
        ProceedingJoinPoint joinPoint
            어드바이스에서 살펴본 MethodInvocation invocation 과 유사한 기능이다.
            내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체와 어떤 메서드가 호출되었는지 정보가 포함되어 있다.

        joinPoint.proceed()
            실제 호출 대상(target)을 호출한다.
        */
        TraceStatus status = null;

        try {
            String message = jp.getSignature().toShortString();
            status = logTrace.begin(message);

            Object result = jp.proceed();

            logTrace.end(status);
            return result;
        } catch(Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
