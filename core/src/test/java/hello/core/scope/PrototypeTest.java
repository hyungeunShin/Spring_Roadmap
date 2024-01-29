package hello.core.scope;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class PrototypeTest {
	@Test
	void prototypeBeanFind() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
		
		//find prototypeBean1
		//init
		//find prototypeBean2
		//init
		//prototypeBean1 : hello.core.scope.PrototypeTest$PrototypeBean@32f232a5
		//prototypeBean2 : hello.core.scope.PrototypeTest$PrototypeBean@43f82e78
		//Closing org.springframework.context.annotation.AnnotationConfigApplicationContext
		
		System.out.println("find prototypeBean1");
		PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
		System.out.println("find prototypeBean2");
		PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
		
		System.out.println("prototypeBean1 : " + prototypeBean1);
		System.out.println("prototypeBean2 : " + prototypeBean2);
		
		assertThat(prototypeBean1).isNotSameAs(prototypeBean2);
		
		//수동으로 소멸시켜야 함
		prototypeBean1.destroy();
		prototypeBean2.destroy();
		
		ac.close();
	}
	
	@Scope("prototype")
	static class PrototypeBean {
		@PostConstruct
		void init() {
			System.out.println("init");
		}
		
		@PreDestroy
		void destroy() {
			System.out.println("destroy");
		}
	}
}
