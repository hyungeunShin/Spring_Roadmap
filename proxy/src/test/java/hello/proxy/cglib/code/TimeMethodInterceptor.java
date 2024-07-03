package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {
    //MethodInterceptor 인터페이스를 구현해서 CGLIB 프록시의 실행 로직을 정의한다.
    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("TimeProxy 실행");
        long start = System.currentTimeMillis();

        //method 를 사용해도 되지만 CGLIB 는 성능상 MethodProxy proxy 를 사용하는 것을 권장한다.
        Object result = proxy.invoke(target, args);

        long end = System.currentTimeMillis();

        log.info("TimeProxy 종료, resultTime : {}", end - start);

        return result;
    }
}
