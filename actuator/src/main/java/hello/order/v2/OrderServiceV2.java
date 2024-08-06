package hello.order.v2;

import hello.order.OrderService;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OrderServiceV2 implements OrderService {
    private AtomicInteger stock = new AtomicInteger(100);

    /*
    @Counted 애노테이션을 측정을 원하는 메서드에 적용한다. 주문과 취소 메서드에 적용했다.
    그리고 메트릭 이름을 지정하면 된다. 여기서는 이전과 같은 my.order 를 적용했다.
    참고로 이렇게 사용하면 tag 에 method 를 기준으로 분류해서 적용한다.
    */
    @Counted("my.order")
    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
    }

    @Counted("my.order")
    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
