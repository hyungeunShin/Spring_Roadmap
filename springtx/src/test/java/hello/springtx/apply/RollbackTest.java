package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RollbackTest {
    /*
    스프링은 왜 체크 예외는 커밋하고 언체크(런타임) 예외는 롤백할까?
        스프링은 기본적으로 체크 예외는 비즈니스 의미가 있을 때 사용하고 런타임(언체크) 예외는 복구 불가능한 예외로 가정한다.

        체크 예외: 비즈니스 의미가 있을 때 사용
            예) 잔액부족
        언체크 예외: 복구 불가능한 예외
            예) 시스템 문제
    */

    @Autowired
    RollbackService service;

    @Test
    void runtimeException() {
        //Rolling back JPA transaction on EntityManager
        Assertions.assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedException() {
        //Committing JPA transaction on EntityManager
        Assertions.assertThatThrownBy(() -> service.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollbackFor() {
        //Rolling back JPA transaction on EntityManager
        Assertions.assertThatThrownBy(() -> service.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class Config {
        @Bean
        RollbackService service() {
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService {
        //런타임 예외 발생 : 롤백
        @Transactional
        public void runtimeException() {
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        //체크 예외 발생 : 커밋
        @Transactional
        public void checkedException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        //체크 예외 rollbackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {}
}
