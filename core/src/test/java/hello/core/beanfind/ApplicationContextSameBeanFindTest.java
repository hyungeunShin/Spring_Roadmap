package hello.core.beanfind;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class ApplicationContextSameBeanFindTest {
	
	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);
	
	@Test
	@DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 중복 오류")
	void findBeanByTypeDuplicate() {
		//타입으로 조회 시 같은 타입의 스프링 빈이 둘 이상이면 NoUniqueBeanDefinitionException 오류가 발생한다.
		//이때는 빈 이름을 지정해야 한다.
		//MemberRepository bean = ac.getBean(MemberRepository.class);
		assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(MemberRepository.class));
	}
	
	@Test
	@DisplayName("타입으로 조회 시 같은 타입이 둘 이상 있으면, 빈 이름 지정하기")
	void findBeanByName() {
		MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class);
		assertThat(memberRepository).isInstanceOf(MemberRepository.class);
	}
	
	@Test
	@DisplayName("특정 타입 모두 조회")
	void findAllBeanByType() {
		Map<String,MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
		
		for(String key : beansOfType.keySet()) {
			System.out.println("key : " + key + ", value : " + beansOfType.get(key));
		}
		
		System.out.println("beansOfType : " + beansOfType);
		assertEquals(2, beansOfType.size());
	}
	
	@Configuration
	static class SameBeanConfig {
		@Bean
		MemberRepository memberRepository1() {
			return new MemoryMemberRepository();
		}
		
		@Bean
		MemberRepository memberRepository2() {
			return new MemoryMemberRepository();
		}
	}
}
