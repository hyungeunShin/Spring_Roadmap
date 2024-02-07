package hello.servlet.domain.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class MemberRepositoryTest {
	MemberRepository memberRepository = MemberRepository.getInstance();

	@Test
	void save() {
		// given
		Member member = new Member("hong", 20);
		
		// when
		Member savedMember = memberRepository.save(member);
		
		// then
		Member findMember = memberRepository.findById(savedMember.getId());
		
		assertEquals(savedMember, findMember);
	}

	@Test
	void findAll() {
		// given
		Member member1 = new Member("member1", 20);
		Member member2 = new Member("member2", 30);
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		// when
		List<Member> result = memberRepository.findAll();
		
		// then
		assertEquals(2, result.size());
		assertThat(result).contains(member1, member2);
	}
	
	@AfterEach
	void afterEach() {
		memberRepository.clearStore();
	}
}
