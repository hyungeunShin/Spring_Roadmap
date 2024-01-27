package hello.core.singleton;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class StatefulServiceTest {
	@Test
	void statefulServiceSingleton() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
		StatefulService statefulService1 = ac.getBean(StatefulService.class);
		StatefulService statefulService2 = ac.getBean(StatefulService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		statefulService1.order("홍길동", 10000);
		statefulService2.order("이순신", 20000);
		
		int price = statefulService1.getPrice();
		System.out.println("price : " + price);
		
		assertEquals(20000, statefulService1.getPrice());
	}
	
	@Configuration
	static class TestConfig {
		@Bean
		StatefulService statefulService() {
			return new StatefulService();
		}
	}
}
