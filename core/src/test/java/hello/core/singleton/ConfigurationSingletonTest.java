package hello.core.singleton;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;

public class ConfigurationSingletonTest {
	@Test
	void configurationTest() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
		OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
		MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		MemberRepository memberRepository1 = memberService.getMemberRepository();
		MemberRepository memberRepository2 = orderService.getMemberRepository();
		
		//memberRepository : hello.core.member.MemoryMemberRepository@5c00384f
		//memberRepository1 : hello.core.member.MemoryMemberRepository@5c00384f
		//memberRepository2 : hello.core.member.MemoryMemberRepository@5c00384f
		System.out.println("memberRepository : " + memberRepository);
		System.out.println("memberRepository1 : " + memberRepository1);
		System.out.println("memberRepository2 : " + memberRepository2);
		
		assertThat(memberRepository).isSameAs(memberRepository1);
		assertThat(memberRepository).isSameAs(memberRepository2);
	}
	
	@Test
	void configurationDeep() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		AppConfig bean = ac.getBean(AppConfig.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		//class hello.core.AppConfig$$SpringCGLIB$$0
		System.out.println("Bean : " + bean.getClass());
	}
}
