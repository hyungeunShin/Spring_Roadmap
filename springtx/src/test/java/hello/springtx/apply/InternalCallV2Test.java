package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
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
class InternalCallV2Test {
    /*
    프록시 방식의 AOP 한계
        @Transactional 를 사용하는 트랜잭션 AOP 는 프록시를 사용한다. 프록시를 사용하면 메서드 내부 호출에 프록시를 적용할 수 없다.
        그렇다면 이 문제를 어떻게 해결할 수 있을까?
        가장 단순한 방법은 내부 호출을 피하기 위해 internal() 메서드를 별도의 클래스로 분리하는 것이다.
    */

    @Autowired
    CallService service;

    @Test
    void printProxy() {
        log.info("service class : {}", service.getClass());
    }

    @Test
    void externalCall() {
        //call external()
        //tx active : false
        //Getting transaction for ...
        //call internal()
        //tx active : true
        //Completing transaction for ...
        service.external();
        /*
        1. 클라이언트인 테스트 코드는 callService.external() 을 호출한다.
        2. callService 는 실제 callService 객체 인스턴스이다.
        3. callService 는 주입 받은 internalService.internal() 을 호출한다.
        4. internalService 는 트랜잭션 프록시이다. internal() 메서드에 @Transactional 이 붙어 있으므로 트랜잭션 프록시는 트랜잭션을 적용한다.
        5. 트랜잭션 적용 후 실제 internalService 객체 인스턴스의 internal() 을 호출한다.
        */
    }

    @TestConfiguration
    static class Config {
        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService() {
            return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService {
        private final InternalService internalService;

        public void external() {
            log.info("call external()");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }
    }

    @Slf4j
    static class InternalService {
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
