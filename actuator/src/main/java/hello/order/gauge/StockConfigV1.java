package hello.order.gauge;

import hello.order.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockConfigV1 {
    @Bean
    public MyStockMetric myStockMetric(OrderService orderService, MeterRegistry registry) {
        return new MyStockMetric(orderService, registry);
    }
}
