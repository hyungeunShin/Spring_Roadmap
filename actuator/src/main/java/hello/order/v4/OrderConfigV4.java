package hello.order.v4;

import hello.order.OrderService;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV4 {
    @Bean
    public OrderService orderService() {
        return new OrderServiceV4();
    }

    //TimedAspect 를 적용해야 @Timed 에 AOP 가 적용된다.
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
