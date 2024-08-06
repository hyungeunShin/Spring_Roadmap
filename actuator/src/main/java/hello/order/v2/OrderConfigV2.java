package hello.order.v2;

import hello.order.OrderService;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV2 {
    @Bean
    public OrderService orderService() {
        return new OrderServiceV2();
    }

    /*
    CountedAspect 를 등록하면 @Counted 를 인지해서 Counter 를 사용하는 AOP 를 적용한다.
    주의! CountedAspect 를 빈으로 등록하지 않으면 @Counted 관련 AOP 가 동작하지 않는다.
    */
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
