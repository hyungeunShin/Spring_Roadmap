package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 같은 Connection 을 파라미터로 전달
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;

    private final MemberRepositoryV2 repository;

    /*
    트랜잭션은 비즈니스 로직이 있는 서비스 계층에서 시작해야 한다. 비즈니스 로직이 잘못되면 해당 비즈니스 로직으로 인해 문제가 되는 부분을 함께 롤백해야 하기 때문이다.
    그런데 트랜잭션을 시작하려면 커넥션이 필요하다.
    애플리케이션에서 DB 트랜잭션을 사용하려면 트랜잭션을 사용하는 동안 같은 커넥션을 유지해야한다. 그래야 같은 세션을 사용할 수 있다.
    결국 서비스 계층에서 커넥션을 만들고, 트랜잭션 커밋 이후에 커넥션을 종료해야 한다.
    */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try {
            //트랜잭션 시작
            con.setAutoCommit(false);

            //비지니스 로직
            bizLogic(con, fromId, toId, money);

            //성공 시 커밋
            con.commit();
        } catch(Exception e) {
            //실패 시 롤백
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        //같은 Connection 을 사용해야 한다.
        Member fromMember = repository.findById(con, fromId);
        Member toMember = repository.findById(con, toId);

        repository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        repository.update(con, toId, toMember.getMoney() + money);
    }

    private void release(Connection con) {
        if(con != null) {
            try {
                //Connection 을 커넥션 풀에 반환하기 전에 다시 AutoCommit 을 true 로 변경
                con.setAutoCommit(true);
                con.close();
            } catch(Exception e) {
                log.error("ERROR", e);
            }
        }
    }

    private void validation(Member toMember) {
        if("ex".equals(toMember.getMemberId())) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
