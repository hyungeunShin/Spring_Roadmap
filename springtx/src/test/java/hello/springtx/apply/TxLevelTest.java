package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
class TxLevelTest {
    @Autowired
    LevelService service;

    @Test
    void orderTest() {
        //tx active : true
        //tx readOnly : false
        service.write();
        //tx active : true
        //tx readOnly : true
        service.read();
    }

    @TestConfiguration
    static class Config {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    /*
    스프링의 @Transactional 은 다음 두 가지 규칙이 있다.
        1. 우선순위 규칙 - 스프링에서 우선순위는 항상 더 구체적이고 자세한 것이 높은 우선순위를 가진다.
        2. 클래스에 적용하면 메서드는 자동 적용

    ※ 참고
    인터페이스에도 @Transactional 을 적용할 수 있다. 이 경우 다음 순서로 적용된다.
        1. 클래스의 메서드에 선언 (우선순위가 가장 높다.)
        2. 클래스에 선언
        3. 인터페이스의 메서드에 선언
        4. 인터페이스에 선언 (우선순위가 가장 낮다.)
    클래스의 메서드를 찾고 만약 없으면 클래스의 타입을 찾고 만약 없으면 인터페이스의 메서드를 찾고 그래도 없으면 인터페이스의 타입을 찾는다.
    하지만 인터페이스에 @Transactional 사용하는 것은 스프링 공식 메뉴얼에서 권장하지 않는 방법이다.
    */
    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {
        @Transactional(readOnly = false)    //readOnly = false 는 기본 옵션이기 때문에 보통 생략
        public void write() {
            log.info("call write()");
            printTxInfo();
        }

        public void read() {
            log.info("call read()");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly : {}", readOnly);
        }
    }
}
