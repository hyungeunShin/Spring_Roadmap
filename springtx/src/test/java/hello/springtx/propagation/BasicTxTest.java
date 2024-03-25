package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class BasicTxTest {
    @Autowired
    PlatformTransactionManager manager;

    @TestConfiguration
    static class Config {
        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus status = manager.getTransaction(new DefaultTransactionAttribute());
        
        log.info("트랜잭션 커밋 시작");
        manager.commit(status);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus status = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백 시작");
        manager.rollback(status);
        log.info("트랜잭션 롤백 완료");
    }

    @Test
    void double_commit() {
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = manager.getTransaction(new DefaultTransactionAttribute());
        
        log.info("트랜잭션1 커밋");
        manager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션2 커밋");
        manager.commit(tx2);
    }

    @Test
    void double_commit_rollback() {
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션1 커밋");
        manager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션2 롤백");
        manager.rollback(tx2);
    }
    
    //신규 트랜잭션인 경우에만 실제 커넥션을 사용해서 물리적으로 커밋과 롤백을 수행한다.
    @Test
    void all_commit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = manager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() : {}", outer.isNewTransaction());

        //내부 트랜잭션을 시작하는 시점에는 이미 외부 트랜잭션이 진행중인 상태이다. 이 경우 내부 트랜잭션은 외부 트랜잭션에 참여한다.
        //Participating in existing transaction
        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = manager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction() : {}", inner.isNewTransaction());

        log.info("내부 트랜잭션 커밋");
        //실제 커밋이 일어나진 않는다.
        manager.commit(inner);
        log.info("외부 트랜잭션 커밋");
        manager.commit(outer);
    }
    
    //전체가 롤백됨
    @Test
    void outer_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 커밋");
        manager.commit(inner);
        log.info("외부 트랜잭션 롤백");
        manager.rollback(outer);
    }

    //전체가 롤백됨
    @Test
    void inner_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = manager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = manager.getTransaction(new DefaultTransactionAttribute());

        //내부 트랜잭션은 트랜잭션 동기화 매니저에 rollbackOnly=true 라는 표시를 해둔다.
        log.info("내부 트랜잭션 롤백");
        manager.rollback(inner);
        log.info("외부 트랜잭션 커밋");
        assertThatThrownBy(() -> manager.commit(outer)).isInstanceOf(UnexpectedRollbackException.class);
    }

    //REQUIRES_NEW 를 통해 외부 트랜잭션과 내부 트랜잭션이 각각 별도의 물리 트랜잭션을 가진다.(DB 커넥션을 따로 사용)
    @Test
    void inner_rollback_requires_new() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = manager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction() : {}", outer.isNewTransaction());

        //Suspending current transaction, creating new transaction with name(기존 트랜잭션을 보류하고 새로운 트랜잭션을 생성한다.)
        log.info("내부 트랜잭션 시작");
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = manager.getTransaction(definition);
        log.info("inner.isNewTransaction() : {}", inner.isNewTransaction());

        log.info("내부 트랜잭션 롤백");
        manager.rollback(inner);
        log.info("외부 트랜잭션 커밋");
        manager.commit(outer);
    }
}
