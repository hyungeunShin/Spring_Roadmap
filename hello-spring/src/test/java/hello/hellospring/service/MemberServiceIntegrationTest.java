package hello.hellospring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;

//@SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.

//Test 클래스에 @Transactional 붙이면
//테스트 시작 전에 트랜잭션을 시작하고 테스트 완료 후에 항상 롤백한다.

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {
	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Test
	@DisplayName("회원가입")
	//@Commit
	public void join() {
		//given
		Member member = new Member();
		member.setName("이길동");
		
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
		
		assertEquals(e.getMessage(), "이미 존재하는 회원입니다.");
	}
}
