package hello.springtx.my;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
class MyServiceTest {
    @Autowired
    MyService service;

    @Test
    void test() {
        //public, protected, default 는 적용
        service.method1();
        service.method2();
        service.method3();
        //private 에 직접 @Transactional 을 붙여도 적용 안됨
        service.method4();
    }

    @TestConfiguration
    static class Config {
        @Bean
        MyService myService() {
            return new MyService();
        }
    }

    @Slf4j
    @Transactional
    static class MyService {
        public void method1() {
            log.info("method1");
            printTxInfo();
        }

        protected void method2() {
            log.info("method2");
            printTxInfo();
        }

        void method3() {
            log.info("method3");
            printTxInfo();
        }

        @Transactional
        private void method4() {
            log.info("method4");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }
    }
}
