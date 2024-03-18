package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV1Test {
    /*
    DriverManagerDataSource -> HikariDataSource 로 변경해도 MemberRepositoryV1 의 코드는 전혀 변경하지 않아도 된다.
    MemberRepositoryV1 는 DataSource 인터페이스에만 의존하기 때문이다.
    이것이 DataSource 를 사용하는 장점이다.
    */
    private MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        //기본 DriverManager : 항상 새로운 커넥션을 획득
        //DriverManagerDataSource 를 사용하면 conn0~5 번호를 통해서 항상 새로운 커넥션이 생성되어서 사용
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //커넥션 풀
        //커넥션 풀 사용시 conn0 커넥션이 재사용
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException, InterruptedException {
        Member member = new Member("홍길동", 10000);
        Member saveMember = repository.save(member);
        assertThat(saveMember).isEqualTo(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember : {}", findMember);
        assertThat(findMember).isEqualTo(member);

        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

        Thread.sleep(1000);
    }
}