package hello.order.v4;

import hello.order.OrderService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
/*
@Timed("my.order") 타입이나 메서드 중에 적용할 수 있다.
타입에 적용하면 해당 타입의 모든 public 메서드에 타이머가 적용된다.
참고로 이 경우 getStock() 에도 타이머가 적용된다.
*/
@Timed("my.order")
public class OrderServiceV4 implements OrderService {
    private AtomicInteger stock = new AtomicInteger(100);

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
        sleep(500);
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
        sleep(200);
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
