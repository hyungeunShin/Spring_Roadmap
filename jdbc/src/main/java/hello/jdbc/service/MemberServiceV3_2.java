package hello.jdbc.service;

import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;

/**
 * 트랜잭션 - 트랜잭션 템플릿(템플릿 콜백 패턴)
 *
 * MemberServiceV3_1 의 문제는 트랜잭션을 시작하고, 비즈니스 로직을 실행하고, 성공하면 커밋하고, 예외가 발생해서 실패하면 롤백한다.
 * 각각의 서비스에서 비즈니스 로직만 달라지지 트랜잭션 try catch 문은 반복된다.
 */
public class MemberServiceV3_2 {
    private final TransactionTemplate transactionTemplate;

    private final MemberRepositoryV3 repository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 repository) {
        //TransactionTemplate 을 사용하려면 transactionManager 가 필요
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.repository = repository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //비즈니스 로직이 정상 수행되면 커밋
        //언체크 예외가 발생하면 롤백, 그 외의 경우 커밋(체크 예외의 경우에는 커밋)
        transactionTemplate.executeWithoutResult((status) -> {
            try {
                bizLogic(fromId, toId, money);
            } catch(SQLException e) {
                //SQLException 체크 예외를 잡아야 하는데 해당 람다에서 체크 예외를 밖으로 던질 수 없기 때문에 언체크 예외로 바꾸어 던지도록 예외를 전환
                throw new IllegalStateException(e);
            }
        });
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
