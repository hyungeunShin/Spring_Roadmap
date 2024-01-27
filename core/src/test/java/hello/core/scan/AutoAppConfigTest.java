package hello.core.scan;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AutoAppConfig;
import hello.core.member.MemberService;

public class AutoAppConfigTest {
	@Test
	void basicScan() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);
		MemberService memberService = ac.getBean(MemberService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		/*
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerProcessor'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.event.internalEventListenerFactory'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.annotation.internalAutowiredAnnotationProcessor'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'org.springframework.context.annotation.internalCommonAnnotationProcessor'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'autoAppConfig'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'rateDiscountPolicy'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'memberServiceImpl'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'memoryMemberRepository'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Autowiring by type from bean name 'memberServiceImpl' via constructor to bean named 'memoryMemberRepository'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Creating shared instance of singleton bean 'orderServiceImpl'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'memoryMemberRepository'
		[main] DEBUG o.s.b.f.s.DefaultListableBeanFactory -- Autowiring by type from bean name 'orderServiceImpl' via constructor to bean named 'rateDiscountPolicy'
		*/
		
		assertThat(memberService).isInstanceOf(MemberService.class);
	}
}
