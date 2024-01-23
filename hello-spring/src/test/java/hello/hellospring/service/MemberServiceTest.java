package hello.hellospring.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;

public class MemberServiceTest {
	MemberService memberService;
	MemoryMemberRepository memberRepository;
	
	@BeforeEach
	public void beforeEach() {
		memberRepository = new MemoryMemberRepository();
		memberService = new MemberService(memberRepository);
	}
	
	@AfterEach
	public void afterEach() {
		memberRepository.clearStore();
	}
	
	@Test
	@DisplayName("회원가입")
	public void join() {
		//given
		Member member = new Member();
		member.setName("홍길동");
		
		//when
		Long saveId = memberService.join(member);
		
		//then
		Member result = memberService.findOne(saveId).get();
		assertEquals(result.getName(), member.getName());
	}
	
	@Test
	@DisplayName("중복 검사")
	public void duplicate() {
		Member member1 = new Member();
		member1.setName("홍길동");
		
		Member member2 = new Member();
		member2.setName("홍길동");
		
		memberService.join(member1);
		IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
		//assertThrows(NullPointerException.class, () -> memberService.join(member2));
		
		assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");
		
		/*
		try {
			memberService.join(member2);
			fail("예외가 발생해야 한다.");
		} catch (IllegalStateException e) {
			assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");
		}
		*/
	}
	
	@Test
	@DisplayName("전체 회원 조회")
	public void findMembers() {
		
	}
	
	@Test
	@DisplayName("회원 찾기")
	public void findOne() {
		
	}
}
