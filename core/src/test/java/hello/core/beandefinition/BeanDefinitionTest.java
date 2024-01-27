package hello.core.beandefinition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import hello.core.AppConfig;

public class BeanDefinitionTest {
	
	AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
	GenericXmlApplicationContext ac2 = new GenericXmlApplicationContext("AppConfig.xml");
	
	@Test
	@DisplayName("자바버전 빈 설정 메타정보")
	void findApplicationBeanWithJava() {
		String[] beanDefinitionNames = ac.getBeanDefinitionNames();
		
		for(String name : beanDefinitionNames) {
			BeanDefinition beanDefinition = ac.getBeanDefinition(name);
			
			if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
				//factoryMethodName=null;
				System.out.println("name : " + name + ", beanDefinition : " + beanDefinition);
			}
		}
	}
	
	@Test
	@DisplayName("XML버전 빈 설정 메타정보")
	void findApplicationBeanWithXml() {
		String[] beanDefinitionNames = ac2.getBeanDefinitionNames();
		
		for(String name : beanDefinitionNames) {
			BeanDefinition beanDefinition = ac2.getBeanDefinition(name);
			
			if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
				//factoryMethodName=memberService;
				//factoryMethodName=orderService;
				//factoryMethodName=memberRepository;
				//factoryMethodName=discountPolicy;
				System.out.println("name : " + name + ", beanDefinition : " + beanDefinition);
			}
		}
	}
}
