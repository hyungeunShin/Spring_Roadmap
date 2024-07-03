package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {
    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderControllerV1 orderController(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderService(logTrace));
        return (OrderControllerV1) Proxy.newProxyInstance(orderController.getClass().getClassLoader()
                , new Class[] {OrderControllerV1.class}
                , new LogTraceFilterHandler(orderController, logTrace, PATTERNS));
    }

    @Bean
    public OrderServiceV1 orderService(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepository(logTrace));
        return (OrderServiceV1) Proxy.newProxyInstance(orderService.getClass().getClassLoader()
                , new Class[] {OrderServiceV1.class}
                , new LogTraceFilterHandler(orderService, logTrace, PATTERNS));
    }

    @Bean
    public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        return (OrderRepositoryV1) Proxy.newProxyInstance(orderRepository.getClass().getClassLoader()
                , new Class[] {OrderRepositoryV1.class}
                , new LogTraceFilterHandler(orderRepository, logTrace, PATTERNS));
    }
}
