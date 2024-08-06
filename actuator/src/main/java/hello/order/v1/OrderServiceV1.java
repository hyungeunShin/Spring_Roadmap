package hello.order.v1;

import hello.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV1 implements OrderService {
    private final MeterRegistry registry;
    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV1(MeterRegistry registry) {
        this.registry = registry;
    }

    /*
    - Counter.builder(name) 를 통해서 카운터를 생성한다. name 에는 메트릭 이름을 지정한다.
    - tag 를 사용했는데 프로메테우스에서 필터할 수 있는 레이블로 사용된다.
    - 주문과 취소는 메트릭 이름은 같고 tag 를 통해서 구분하도록 했다.
    - register(registry) : 만든 카운터를 MeterRegistry 에 등록한다. 이렇게 등록해야 실제 동작한다.
    - increment() : 카운터의 값을 하나 증가한다.
    */
    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();

        Counter.builder("my.order")
               .tag("class", this.getClass().getName())
               .tag("method", "order")
               .description("order")
               .register(registry)
               .increment();
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();

        Counter.builder("my.order")
               .tag("class", this.getClass().getName())
               .tag("method", "cancel")
               .description("order")
               .register(registry)
               .increment();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
