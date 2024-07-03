package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class TimeAdvice implements MethodInterceptor {
    //패키지 이름 주의

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long start = System.currentTimeMillis();

        /*
        invocation.proceed() 를 호출하면 target 클래스를 호출하고 그 결과를 받는다.
        그런데 기존에 보았던 코드들과 다르게 target 클래스의 정보가 보이지 않는다.
        target 클래스의 정보는 MethodInvocation invocation 안에 모두 포함되어 있다.
        프록시 팩토리로 프록시를 생성하는 단계에서 이미 target 정보를 파라미터로 전달받기 때문이다.
        */
        Object result = invocation.proceed();

        long end = System.currentTimeMillis();

        log.info("TimeProxy 종료, resultTime : {}", end - start);

        return result;
    }
}
