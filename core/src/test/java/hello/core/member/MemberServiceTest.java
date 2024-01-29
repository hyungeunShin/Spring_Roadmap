package hello.core.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;

public class MemberServiceTest {
	
	private MemberService memberService;
	
	@BeforeEach
	void before() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		memberService = ac.getBean("memberService", MemberService.class);
		((AnnotationConfigApplicationContext) ac).close();
	}
	
	@Test
	@DisplayName("회원가입")
	void join() {
		//given
		Member member = new Member(1L, "홍길동", Grade.VIP);
		
		//when
		memberService.join(member);
		Member findMember = memberService.findMember(member.getId());
		
		//then
		assertEquals(member, findMember);
	}
}
