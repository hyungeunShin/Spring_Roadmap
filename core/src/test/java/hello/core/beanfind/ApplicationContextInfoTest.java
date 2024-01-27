package hello.core.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;

public class ApplicationContextInfoTest {
	
	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
	
	@Test
	@DisplayName("모든 빈 출력")
	void findAll() {
		//ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회한다.
		String[] names = ac.getBeanDefinitionNames();
		
		for(String name : names) {
			//ac.getBean() : 빈 이름으로 빈 객체(인스턴스)를 조회한다.
			Object bean = ac.getBean(name);
			System.out.println("name : " + name + ", object : " + bean);
		}
	}
	
	@Test
	@DisplayName("어플리케이션 빈 출력")
	void findApplicationBean() {
		String[] names = ac.getBeanDefinitionNames();
		
		for(String name : names) {
			BeanDefinition beanDefinition = ac.getBeanDefinition(name);
			
			//ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
			//ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
			if(beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
				Object bean = ac.getBean(name);
				System.out.println("name : " + name + ", object : " + bean);
			}
		}
	}
}
