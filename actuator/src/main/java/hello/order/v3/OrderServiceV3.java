package hello.order.v3;

import hello.order.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV3 implements OrderService {
    private final MeterRegistry registry;
    private AtomicInteger stock = new AtomicInteger(100);

    public OrderServiceV3(MeterRegistry registry) {
        this.registry = registry;
    }

    /*
    - Timer.builder(name) 를 통해서 타이머를 생성한다. name 에는 메트릭 이름을 지정한다.
    - tag 를 사용했는데, 프로메테우스에서 필터할 수 있는 레이블로 사용된다.
    - 주문과 취소는 메트릭 이름은 같고 tag 를 통해서 구분하도록 했다.
    - register(registry) : 만든 타이머를 MeterRegistry 에 등록한다. 이렇게 등록해야 실제 동작한다.
    - 타이머를 사용할 때는 timer.record() 를 사용하면 된다. 그 안에 시간을 측정할 내용을 함수로 포함하면 된다.
    */
    @Override
    public void order() {
        Timer timer = Timer.builder("my.order")
                           .tag("class", this.getClass().getName())
                           .tag("method", "order")
                           .description("order")
                           .register(registry);

        timer.record(() -> {
            log.info("주문");
            stock.decrementAndGet();
            sleep(500);
        });
    }

    @Override
    public void cancel() {
        Timer timer = Timer.builder("my.order")
                           .tag("class", this.getClass().getName())
                           .tag("method", "cancel")
                           .description("order")
                           .register(registry);

        timer.record(() -> {
            log.info("취소");
            stock.incrementAndGet();
            sleep(500);
        });
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }

    private static void sleep(int l) {
        try {
            Thread.sleep(l + new Random().nextInt(200));
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
