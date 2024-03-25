package hello.springtx.my;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
class TransactionTest {
    @Autowired
    MyService service;

    @Test
    void test1() {
        service.method1();
    }

    @Test
    void test2() {
        service.method2();
    }

    @Test
    void test3() {
        service.external();
    }

    @TestConfiguration
    static class Config {
        @Bean
        MyService service() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            dataSource.setPassword("");
            PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
            return new MyService(transactionManager);
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class MyService {
        private final PlatformTransactionManager transactionManager;

        public void method1() {
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            printTxInfo();
            transactionManager.rollback(status);
        }

        public void method2() {
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionManager.rollback(status);
            printTxInfo();
        }

        public void external() {
            log.info("external");
            printTxInfo();
            internal();
        }

        public void internal() {
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            log.info("internal");
            printTxInfo();
            transactionManager.rollback(status);
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active : {}", txActive);
        }
    }
}
