package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class TxBasicTest {
    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        /*
        선언적 트랜잭션 방식에서 스프링 트랜잭션은 AOP 를 기반으로 동작한다.
        @Transactional 을 메서드나 클래스에 붙이면 해당 객체는 트랜잭션 AOP 적용의 대상이 되고 결과적으로 실제 객체 대신에 트랜잭션을 처리해주는 프록시 객체가 스프링 빈에 등록된다.
        그리고 주입을 받을 때도 실제 객체 대신에 프록시 객체가 주입된다.
        */
        //class hello.springtx.apply.TxBasicTest$BasicService$$SpringCGLIB$$0
        log.info("aop class : {}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest() {
        //tx active : true
        basicService.tx();
        //tx active : false
        basicService.nonTx();
    }

    @TestConfiguration
    static class Config {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {
        //@Transactional 애노테이션이 특정 클래스나 메서드에 하나라도 있으면 트랜잭션 AOP 는 프록시를 만들어서 스프링 컨테이너에 등록한다.
        @Transactional
        public void tx() {
            log.info("call tx()");
            //현재 쓰레드에 트랜잭션이 적용되어 있는지 확인할 수 있는 기능이다. 결과가 true 면 트랜잭션이 적용되어 있는 것이다. 트랜잭션의 적용 여부를 가장 확실하게 확인할 수 있다.
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }

        public void nonTx() {
            log.info("call nonTx()");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }
    }
}
