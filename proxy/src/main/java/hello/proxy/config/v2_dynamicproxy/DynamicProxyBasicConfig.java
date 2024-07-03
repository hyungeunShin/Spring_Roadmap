package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {
    /*
    JDK 동적 프록시
        지금까지 프록시를 사용해서 기존 코드를 변경하지 않고, 로그 추적기라는 부가 기능을 적용할 수 있었다.
        그런데 문제는 대상 클래스 수 만큼 로그 추적을 위한 프록시 클래스를 만들어야 한다는 점이다.
        로그 추적을 위한 프록시 클래스들의 소스코드는 거의 같은 모양을 하고 있다.

        JDK 동적 프록시 기술을 사용하면 적용 대상 만큼 프록시 객체를 만들지 않아도 된다.
        그리고 같은 부가 기능 로직을 한번만 개발해서 공통으로 적용할 수 있다.
        만약 적용 대상이 100개여도 동적 프록시를 통해서 생성하고 각각 필요한 InvocationHandler 만 만들어서 넣어주면 된다.
        결과적으로 프록시 클래스를 수 없이 만들어야 하는 문제도 해결하고 부가 기능 로직도 하나의 클래스에 모아서 단일책임 원칙(SRP)도 지킬 수 있게 된다.

        한계
        JDK 동적 프록시는 인터페이스가 필수이다.
        그렇다면 V2처럼 인터페이스 없이 클래스만 있는 경우에는 어떻게 동적 프록시를 적용할 수 있을까?
        이것은 일반적인 방법으로는 어렵고 CGLIB 라는 바이트코드를 조작하는 특별한 라이브러리를 사용해야 한다.

    CGLIB(Code Generator Library)
        - CGLIB 는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
        - CGLIB 를 사용하면 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
        - CGLIB 는 원래는 외부 라이브러리인데, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다. 따라서 스프링을 사용한다면 별도의 외부 라이브러리를 추가하지 않아도 사용할 수 있다.

        제약
        - 부모 클래스의 생성자를 체크해야 한다. -> CGLIB 는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다.
        - 클래스에 final 키워드가 붙으면 상속이 불가능하다. CGLIB 에서는 예외가 발생한다.
        - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다. CGLIB 에서는 프록시 로직이 동작하지 않는다.

    문제점
        - 인터페이스가 있는 경우에는 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB 를 적용하려면 어떻게 해야할까?
        - 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해 JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB 가 제공하는 MethodInterceptor 를 각각 중복으로 만들어서 관리해야 할까?
        - 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면?
    */
    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderService(logTrace));
        return (OrderControllerV1) Proxy.newProxyInstance(orderController.getClass().getClassLoader()
                , new Class[] {OrderControllerV1.class}
                , new LogTraceBasicHandler(orderController, logTrace));
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepository(logTrace));
        return (OrderServiceV1) Proxy.newProxyInstance(orderService.getClass().getClassLoader()
                , new Class[] {OrderServiceV1.class}
                , new LogTraceBasicHandler(orderService, logTrace));
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        return (OrderRepositoryV1) Proxy.newProxyInstance(orderRepository.getClass().getClassLoader()
                , new Class[] {OrderRepositoryV1.class}
                , new LogTraceBasicHandler(orderRepository, logTrace));
    }
}
