package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v1.*;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {
    /*
    ※ 프록시 팩토리
        - 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB 를 적용하려면 어떻게 해야할까?
            스프링은 유사한 구체적인 기술들이 있을 때 그것들을 통합해서 일관성 있게 접근할 수 있고 더욱 편리하게 사용할 수 있는 추상화된 기술을 제공한다.
            스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리(ProxyFactory)라는 기능을 제공한다.
            이전에는 상황에 따라서 JDK 동적 프록시를 사용하거나 CGLIB 를 사용해야 했다면 이제는 이 프록시 팩토리 하나로 편리하게 동적 프록시를 생성할 수 있다.
            프록시 팩토리는 인터페이스가 있으면 JDK 동적 프록시를 사용하고 구체 클래스만 있다면 CGLIB 를 사용한다.
            그리고 이 설정을 변경할 수도 있다.

        - 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해 JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB 가 제공하는 MethodInterceptor 를 각각 중복으로 만들어서 관리해야 할까?
            스프링은 이 문제를 해결하기 위해 부가 기능을 적용할 때 Advice 라는 새로운 개념을 도입했다.
            개발자는 InvocationHandler 나 MethodInterceptor 를 신경쓰지 않고 Advice 만 만들면 된다.
            결과적으로 InvocationHandler 나 MethodInterceptor 는 Advice 를 호출하게 된다.
            프록시 팩토리를 사용하면 Advice 를 호출하는 전용 InvocationHandler, MethodInterceptor 를 내부에서 사용한다.

        - 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?
            앞서 특정 메서드 이름의 조건에 맞을 때만 프록시 부가 기능이 적용되는 코드를 직접 만들었다.
            스프링은 Pointcut 이라는 개념을 도입해서 이 문제를 일관성 있게 해결한다.

    - 요소
        - 포인트컷(Pointcut)
            어디에 부가 기능을 적용할지, 어디에 부가 기능을 적용하지 않을지 판단하는 필터링 로직이다.
            주로 클래스와 메서드 이름으로 필터링 한다. 이름 그대로 어떤 포인트(Point)에 기능을 적용할지 하지 않을지 잘라서(cut) 구분하는 것이다.
        - 어드바이스(Advice)
            이전에 본 것 처럼 프록시가 호출하는 부가 기능이다. 단순하게 프록시 로직이라 생각하면 된다.
        - 어드바이저(Advisor)
            단순하게 하나의 포인트컷과 하나의 어드바이스를 가지고 있는 것이다. 쉽게 이야기해서 포인트컷1 + 어드바이스1이다.
        - 부가 기능 로직을 적용해야 하는데, 포인트컷으로 어디에? 적용할지 선택하고, 어드바이스로 어떤 로직을 적용할지 선택하는 것이다. 그리고 어디에? 어떤 로직?을 모두 알고 있는 것이 어드바이저이다.
        - 역할과 책임
            포인트컷은 대상 여부를 확인하는 필터 역할만 담당한다.
            어드바이스는 깔끔하게 부가 기능 로직만 담당한다.
            둘을 합치면 어드바이저가 된다. 스프링의 어드바이저는 하나의 포인트컷 + 하나의 어드바이스로 구성된다.
    */
    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderService(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderController);
        proxyFactory.addAdvisor(getAdvisor(logTrace));

        OrderControllerV1 proxy = (OrderControllerV1) proxyFactory.getProxy();
        log.info("proxy : {}, target : {}", proxy.getClass(), orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepository(logTrace));
        ProxyFactory proxyFactory = new ProxyFactory(orderService);
        proxyFactory.addAdvisor(getAdvisor(logTrace));

        OrderServiceV1 proxy = (OrderServiceV1) proxyFactory.getProxy();
        log.info("proxy : {}, target : {}", proxy.getClass(), orderService.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        ProxyFactory proxyFactory = new ProxyFactory(orderRepository);
        proxyFactory.addAdvisor(getAdvisor(logTrace));

        OrderRepositoryV1 proxy = (OrderRepositoryV1) proxyFactory.getProxy();
        log.info("proxy : {}, target : {}", proxy.getClass(), orderRepository.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
