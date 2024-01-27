package hello.hellospring.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import hello.hellospring.domain.Member;

public class MemoryMemberRepositoryTest {
	MemoryMemberRepository memberRepository = new MemoryMemberRepository();
	
	@AfterEach
	public void afterEach() {
		memberRepository.clearStore();
	}
	
	@Test
	public void save() {
		Member member = new Member();
		member.setName("홍길동");
		
		memberRepository.save(member);
		
		Member result = memberRepository.findById(member.getId()).get();
		
		Assertions.assertEquals(member, result);
		assertThat(result).isEqualTo(member);
	}
	
	@Test
	public void findByName() {
		Member member1 = new Member();
		member1.setName("홍길동");
		memberRepository.save(member1);
		
		Member member2 = new Member();
		member2.setName("박길동");
		memberRepository.save(member2);
		
		Member result = memberRepository.findByName("홍길동").get();
		assertEquals(member1, result);
	}
	
	@Test
	public void findAll() {
		Member member1 = new Member();
		member1.setName("홍길동");
		memberRepository.save(member1);
		
		Member member2 = new Member();
		member2.setName("박길동");
		memberRepository.save(member2);
		
		List<Member> list = memberRepository.findAll();
		assertEquals(2, list.size());
	}
}
