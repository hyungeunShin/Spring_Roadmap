package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("홍길동", 10000);
        Member saveMember = repository.save(member);
        assertThat(saveMember).isEqualTo(member);

        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember : {}", findMember);
        assertThat(findMember).isEqualTo(member);

        int result = repository.update(member.getMemberId(), 20000);
        log.info("update result : {}", result);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);
    }
}