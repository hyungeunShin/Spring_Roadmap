package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
//@Import(AspectV4Pointcut.class)
//@Import({AspectV5Order.LogAspect.class, AspectV5Order.TxAspect.class})
@Import(AspectV6Advice.class)
@SpringBootTest
class AopTest {
    @Autowired
    OrderService service;

    @Autowired
    OrderRepository repository;

    @Test
    void aopInfo() {
        //@Import(AspectV1.class) 있으면 true, 없으면 false
        log.info("isAopProxy? orderService : {}", AopUtils.isAopProxy(service));
        log.info("isAopProxy? orderRepository : {}", AopUtils.isAopProxy(repository));
    }

    @Test
    void success() {
        service.orderItem("itemA");
    }

    @Test
    void exception() {
        Assertions.assertThatThrownBy(() -> service.orderItem("ex")).isInstanceOf(IllegalStateException.class);
    }
}
