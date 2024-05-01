package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV2;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ContextV2Test {
    /*
    ContextV2 는 파라미터에 Strategy 를 전달받는 방식
        - 실행할 때 마다 전략을 유연하게 변경할 수 있다.
        - 단점 역시 실행할 때 마다 전략을 계속 지정해주어야 한다는 점이다.

    ContextV2 는 변하지 않는 템플릿 역할을 한다.
    그리고 변하는 부분은 파라미터로 넘어온 Strategy 의 코드를 실행해서 처리한다.
    이렇게 다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 콜백(callback)이라 한다.

    템플릿 콜백 패턴
        스프링에서는 ContextV2 와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라 한다.
        전략 패턴에서 Context 가 템플릿 역할을 하고 Strategy 부분이 콜백으로 넘어온다 생각하면 된다.

    ※ 참고
    템플릿 콜백 패턴은 GOF 패턴은 아니고 스프링 내부에서 이런 방식을 자주 사용하기 때문에 스프링 안에서만 이렇게 부른다.
    전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴이라 생각하면 된다.
    스프링에서는 JdbcTemplate, RestTemplate, TransactionTemplate, RedisTemplate 처럼 다양한 템플릿 콜백 패턴이 사용된다.
    스프링에서 이름에 XxxTemplate 가 있다면 템플릿 콜백 패턴으로 만들어져있다 생각하면 된다.
    */

    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });

        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
