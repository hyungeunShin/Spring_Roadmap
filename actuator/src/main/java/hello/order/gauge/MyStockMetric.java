package hello.order.gauge;

import hello.order.OrderService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class MyStockMetric {
    private final OrderService orderService;
    private final MeterRegistry registry;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Gauge.builder("my.stock", orderService, service -> {
            log.info("stock gauge call");
            return service.getStock().get();
        }).register(registry);
    }
}
