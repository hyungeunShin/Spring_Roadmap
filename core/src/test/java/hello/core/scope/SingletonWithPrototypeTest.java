package hello.core.scope;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class SingletonWithPrototypeTest {
	@Test
	void prototypeFind() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
		PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
		prototypeBean1.addCount();
		assertEquals(1, prototypeBean1.getCount());
		
		PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
		prototypeBean2.addCount();
		assertEquals(1, prototypeBean2.getCount());
		
		ac.close();
	}
	
	@Test
	void singletonClientUsePrototype() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class);
		
		//싱글톤 빈이 생성되면서 이미 의존성을 주입받음
		//싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에, 프로토타입 빈이 새로 생성되기는 하지만, 싱글톤 빈과 함께 계속 유지되는 것
		//같은 프로토타입을 사용
		
		ClientBean clientBean1 = ac.getBean(ClientBean.class);
		int count1 = clientBean1.logic();
		assertEquals(1, count1);
		
		ClientBean clientBean2 = ac.getBean(ClientBean.class);
		int count2 = clientBean2.logic();
		assertEquals(2, count2);
		
		ac.close();
	}
	
	@Scope("singleton")
	static class ClientBean {
		private final PrototypeBean prototypeBean;

		public ClientBean(PrototypeBean prototypeBean) {
			this.prototypeBean = prototypeBean;
		}
		
		public int logic() {
			prototypeBean.addCount();
			return prototypeBean.getCount();
		}
	}
	
	@Scope("prototype")
	static class PrototypeBean {
		private int count = 0;
		
		void addCount() {
			count++;
		}
		
		int getCount() {
			return count;
		}
		
		@PostConstruct
		void init() {
			System.out.println("init " + this);
		}
		
		@PreDestroy
		void destroy() {
			System.out.println("destroy");
		}
	}
}
