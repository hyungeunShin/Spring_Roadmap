package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
class InternalCallV1Test {
    /*
    @Transactional 을 사용하면 스프링의 트랜잭션 AOP 가 적용된다. 트랜잭션 AOP 는 기본적으로 프록시 방식의 AOP 를 사용한다.
    @Transactional 을 적용하면 프록시 객체가 요청을 먼저 받아서 트랜잭션을 처리하고 실제 객체를 호출해준다.
    ★ 따라서 트랜잭션을 적용하려면 항상 프록시를 통해서 대상 객체(Target)을 호출해야 한다. ★
    이렇게 해야 프록시에서 먼저 트랜잭션을 적용하고 이후에 대상 객체를 호출하게 된다.
    만약 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 AOP 가 적용되지 않고 트랜잭션도 적용되지 않는다.

    AOP 를 적용하면 스프링은 대상 객체 대신에 프록시를 스프링 빈으로 등록한다.
    따라서 스프링은 의존관계 주입시에 항상 실제 객체 대신에 프록시 객체를 주입한다.
    프록시 객체가 주입되기 때문에 대상 객체를 직접 호출하는 문제는 일반적으로 발생하지 않는다.
    ★ 하지만 대상 객체의 내부에서 타겟 자신의 메서드 호출이 발생하면 프록시를 거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다. ★
    이렇게 되면 @Transactional 이 있어도 트랜잭션이 적용되지 않는다.
    */

    @Autowired
    CallService service;

    @Test
    void printProxy() {
        log.info("service class : {}", service.getClass());
    }

    @Test
    void internalCall() {
        /*
        1. 클라이언트인 테스트 코드는 callService.internal() 을 호출한다. 여기서 callService 는 트랜잭션 프록시이다.
        2. callService 의 트랜잭션 프록시가 호출된다.
        3. internal() 메서드에 @Transactional 이 붙어 있으므로 트랜잭션 프록시는 트랜잭션을 적용한다.
        4. 트랜잭션 적용 후 실제 callService 객체 인스턴스의 internal() 을 호출한다.
        */
        service.internal();
    }

    @Test
    void externalCall() {
        //call external()
        //tx active : false
        //call internal()
        //tx active : false
        service.external();
        /*
        1. 클라이언트인 테스트 코드는 callService.external() 을 호출한다. 여기서 callService 는 트랜잭션 프록시이다.
        2. callService 의 트랜잭션 프록시가 호출된다.
        3. external() 메서드에는 @Transactional 이 없다. 따라서 트랜잭션 프록시는 트랜잭션을 적용하지 않는다.
        4. 트랜잭션 적용하지 않고 실제 callService 객체 인스턴스의 external() 을 호출한다.
        5. external() 은 내부에서 internal() 메서드를 호출한다. 그런데 여기서 문제가 발생한다.

        자바 언어에서 메서드 앞에 별도의 참조가 없으면 this 라는 뜻으로 자기 자신의 인스턴스를 가리킨다.
        결과적으로 자기 자신의 내부 메서드를 호출하는 this.internal() 이 되는데 여기서 this 는 자기 자신을 가리키므로
        실제 대상 객체(target)의 인스턴스를 뜻한다.
        결과적으로 이러한 내부 호출은 프록시를 거치지 않는다. 따라서 트랜잭션을 적용할 수 없다.
        결과적으로 target 에 있는 internal() 을 직접 호출하게 된 것이다.
        */
    }

    @TestConfiguration
    static class Config {
        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {
        public void external() {
            log.info("call external()");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal() {
            log.info("call internal()");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }
    }
}
