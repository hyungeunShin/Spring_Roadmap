package hello.core.singleton;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;
import hello.core.member.MemberService;

public class SingletonTest {
	@Test
	@DisplayName("스프링 없는 순수한 DI 컨테이너")
	void pureContainer() {
		AppConfig appConfig = new AppConfig();
		MemberService memberService1 = appConfig.memberService();
		MemberService memberService2 = appConfig.memberService();
		
		//참조값이 다름
		//memberService1 : hello.core.member.MemberServiceImpl@7364985f
		//memberService2 : hello.core.member.MemberServiceImpl@709ba3fb
		System.out.println("memberService1 : " + memberService1);
		System.out.println("memberService2 : " + memberService2);
		
		assertThat(memberService1).isNotSameAs(memberService2);
	}
	
	@Test
	@DisplayName("싱글톤 적용한 객체")
	void singletonServiceTest() {
		SingletonService service1 = SingletonService.getInstance();
		SingletonService service2 = SingletonService.getInstance();
		
		//service1 : hello.core.singleton.SingletonService@56528192
		//service2 : hello.core.singleton.SingletonService@56528192
		System.out.println("service1 : " + service1);
		System.out.println("service2 : " + service2);
		
		assertThat(service1).isSameAs(service2);
	}
	
	@Test
	@DisplayName("스프링 컨테이너와 싱글톤")
	void springContainer() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		MemberService memberService1 = ac.getBean("memberService", MemberService.class);
		MemberService memberService2 = ac.getBean("memberService", MemberService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		//memberService1 : hello.core.member.MemberServiceImpl@2692b61e
		//memberService2 : hello.core.member.MemberServiceImpl@2692b61e
		System.out.println("memberService1 : " + memberService1);
		System.out.println("memberService2 : " + memberService2);
		
		assertThat(memberService1).isSameAs(memberService2);
	}
}
