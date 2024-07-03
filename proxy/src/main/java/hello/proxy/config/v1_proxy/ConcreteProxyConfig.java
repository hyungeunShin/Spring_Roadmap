package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {
    /*
    인터페이스 기반 프록시 vs 클래스 기반 프록시(v1 vs v2)
        - 인터페이스가 없어도 클래스 기반으로 프록시를 생성할 수 있다.
        - 클래스 기반 프록시는 해당 클래스에만 적용할 수 있다.
          인터페이스 기반 프록시는 인터페이스만 같으면 모든 곳에 적용할 수 있다.
        - 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
            - 부모 클래스의 생성자를 호출해야 한다.
            - 클래스에 final 키워드가 붙으면 상속이 불가능하다.
            - 메서드에 final 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다.

        이렇게 보면 인터페이스 기반의 프록시가 더 좋아보인다.
        인터페이스 기반의 프록시는 상속이라는 제약에서 자유롭다.
        프로그래밍 관점에서도 인터페이스를 사용하는 것이 역할과 구현을 명확하게 나누기 때문에 더 좋다.
        인터페이스 기반 프록시의 단점은 인터페이스가 필요하다는 그 자체이다.
        인터페이스가 없으면 인터페이스 기반 프록시를 만들 수 없다.

        이론적으로는 모든 객체에 인터페이스를 도입해서 역할과 구현을 나누는 것이 좋다.
        이렇게 하면 역할과 구현을 나누어서 구현체를 매우 편리하게 변경할 수 있다.
        하지만 실제로는 구현을 거의 변경할 일이 없는 클래스도 많다.
        인터페이스를 도입하는 것은 구현을 변경할 가능성이 있을 때 효과적이다.
        하지만 구현을 변경할 가능성이 거의 없는 코드에 무작정 인터페이스를 사용하는 것은 번거롭고 그렇게 실용적이지 않다.
        이런곳에는 실용적인 관점에서 인터페이스를 사용하지 않고 구체 클래스를 바로 사용하는 것이 좋다 생각한다.
        (물론 인터페이스를 도입하는 다양한 이유가 있다. 여기서 핵심은 인터페이스가 항상 필요하지는 않다는 것이다.)

    너무 많은 프록시 클래스
    v1 과 v2까지 프록시를 사용해서 기존 코드를 변경하지 않고 로그 추적기라는 부가 기능을 적용할 수 있었다.
    그런데 문제는 프록시 클래스를 너무 많이 만들어야 한다는 점이다.
    잘 보면 프록시 클래스가 하는 일은 LogTrace 를 사용하는 것인데 그 로직이 모두 똑같다.
    대상 클래스만 다를 뿐이다.
    만약 적용해야 하는 대상 클래스가 100개라면 프록시 클래스도 100개를 만들어야한다.
    프록시 클래스를 하나만 만들어서 모든 곳에 적용하는 방법은 없을까?
    */
    @Bean
    public OrderControllerV2 orderController(LogTrace logTrace) {
        OrderControllerV2 orderControllerV2 = new OrderControllerV2(orderService(logTrace));
        return new OrderControllerConcreteProxy(orderControllerV2, logTrace);
    }

    @Bean
    public OrderServiceV2 orderService(LogTrace logTrace) {
        OrderServiceV2 orderServiceV2 = new OrderServiceV2(orderRepository(logTrace));
        return new OrderServiceConcreteProxy(orderServiceV2, logTrace);
    }

    @Bean
    public OrderRepositoryV2 orderRepository(LogTrace logTrace) {
        OrderRepositoryV2 orderRepositoryV2 = new OrderRepositoryV2();
        return new OrderRepositoryConcreteProxy(orderRepositoryV2, logTrace);
    }
}
