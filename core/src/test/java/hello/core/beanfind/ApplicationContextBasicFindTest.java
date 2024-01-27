package hello.core.beanfind;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class ApplicationContextBasicFindTest {
	
	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
	
	@Test
	@DisplayName("이름으로 조회")
	void findBeanByName() {
		MemberService memberService = ac.getBean("memberService", MemberService.class);
		assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
	}
	
	@Test
	@DisplayName("타입으로 조회")
	void findBeanByType() {
		MemberService memberService = ac.getBean(MemberService.class);
		assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
	}
	
	@Test
	@DisplayName("구현 타입으로 조회")
	void findBeanByName2() {
		MemberServiceImpl memberServiceImpl = ac.getBean("memberService", MemberServiceImpl.class);
		assertThat(memberServiceImpl).isInstanceOf(MemberServiceImpl.class);
	}
	
	@Test
	@DisplayName("커스텀 이름으로 조회")
	void findBeanByCustomName() {
		//존재하지 않는 빈을 get하면 NoSuchBeanDefinitionException
		//MemberService memberService = ac.getBean("xxxx", MemberService.class);
		
		assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxx", MemberService.class));
	}
}
