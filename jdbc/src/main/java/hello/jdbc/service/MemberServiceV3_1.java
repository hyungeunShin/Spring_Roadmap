package hello.jdbc.service;

import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;

/**
 * 트랜잭션 - 트랜잭션 매니저(트랜잭션 추상화)
 *
 * MemberServiceV2 의 문제는 트랜잭션을 사용하기 위해서 JDBC 기술에 의존하고 있다.
 * 향후 JDBC 에서 JPA 같은 다른 데이터 접근 기술로 변경하면 서비스 계층의 트랜잭션 관련 코드도 모두 함께 수정해야 하므로 트랜잭션을 추상화한다.
 * JPA 로 기술 변경시 의존관계 주입만 DataSourceTransactionManager 에서 JpaTransactionManager 로 변경해주면 된다.
 */
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    //트랜잭션 매니저를 주입 받는다. 지금은 JDBC 기술을 사용하기 때문에 DataSourceTransactionManager 구현체를 주입 받아야 한다.
    private final PlatformTransactionManager transactionManager;

    private final MemberRepositoryV3 repository;

    /*
    - 스프링은 트랜잭션 동기화 매니저를 제공한다. 이것은 쓰레드 로컬(ThreadLocal)을 사용해서 커넥션을 동기화해준다.
      트랜잭션 매니저는 내부에서 이 트랜잭션 동기화 매니저를 사용한다.
    - 트랜잭션 동기화 매니저는 쓰레드 로컬을 사용하기 때문에 멀티쓰레드 상황에 안전하게 커넥션을 동기화 할 수 있다.
      따라서 커넥션이 필요하면 트랜잭션 동기화 매니저를 통해 커넥션을 획득하면 된다. 따라서 이전처럼 파라미터로 커넥션을 전달하지 않아도 된다.

    동작 방식
        1. 서비스 계층에서 transactionManager.getTransaction() 을 호출해서 트랜잭션을 시작한다.
        2. 트랜잭션을 시작하려면 먼저 데이터베이스 커넥션이 필요하다. 트랜잭션 매니저는 내부에서 데이터소스를 사용해서 커넥션을 생성한다.
        3. 커넥션을 수동 커밋 모드로 변경해서 실제 데이터베이스 트랜잭션을 시작한다.
        4. 커넥션을 트랜잭션 동기화 매니저에 보관한다.
        5. 트랜잭션 동기화 매니저는 쓰레드 로컬에 커넥션을 보관한다. 따라서 멀티 쓰레드 환경에 안전하게 커넥션을 보관 할 수 있다.
        6. 서비스는 비즈니스 로직을 실행하면서 리포지토리의 메서드들을 호출한다. 이때 커넥션을 파라미터로 전달하지 않는다.
        7. 리포지토리 메서드들은 트랜잭션이 시작된 커넥션이 필요하다.
           리포지토리는 DataSourceUtils.getConnection() 을 사용해서 트랜잭션 동기화 매니저에 보관된 커넥션을 꺼내서 사용한다.
           이 과정을 통해서 자연스럽게 같은 커넥션을 사용하고, 트랜잭션도 유지된다.
        8. 획득한 커넥션을 사용해서 SQL을 데이터베이스에 전달해서 실행한다.
        9. 비즈니스 로직이 끝나고 트랜잭션을 종료한다. 트랜잭션은 커밋하거나 롤백하면 종료된다.
        10. 트랜잭션을 종료하려면 동기화된 커넥션이 필요하다. 트랜잭션 동기화 매니저를 통해 동기화된 커넥션을 획득한다.
        11. 획득한 커넥션을 통해 데이터베이스에 트랜잭션을 커밋하거나 롤백한다.
        12. 전체 리소스를 정리한다.
            - 트랜잭션 동기화 매니저를 정리한다. 쓰레드 로컬은 사용후 꼭 정리해야 한다.
            - con.setAutoCommit(true) 로 되돌린다. 커넥션 풀을 고려해야 한다.
            - con.close() 를 호출해셔 커넥션을 종료한다. 커넥션 풀을 사용하는 경우 con.close() 를 호출하면 커넥션 풀에 반환된다.
    */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            //비지니스 로직
            bizLogic(fromId, toId, money);

            //성공 시 커밋
            transactionManager.commit(status);
        } catch(Exception e) {
            //실패 시 롤백
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = repository.findById(fromId);
        Member toMember = repository.findById(toId);

        repository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        repository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if("ex".equals(toMember.getMemberId())) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
