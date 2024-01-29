package hello.core.scope;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;

public class PrototypeProviderTest {
	@Test
	void providerTest() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
		
		ClientBean clientBean1 = ac.getBean(ClientBean.class);
		int count1 = clientBean1.logic();
		assertEquals(1, count1);
		
		ClientBean clientBean2 = ac.getBean(ClientBean.class);
		int count2 = clientBean2.logic();
		assertEquals(1, count2);
		
		ac.close();
	}
	
	/*
	@Scope("singleton")
	static class ClientBean {
		@Autowired
		private ApplicationContext ac;
		
		public int logic() {
			//의존관계를 외부에서 주입(DI) 받는게 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 Dependency Lookup(DL)이라한다.
			//스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고, 단위 테스트도 어려워진다.
			PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
			prototypeBean.addCount();
			int count = prototypeBean.getCount();
			return count;
		}
	}
	*/
	
	@Scope("singleton")
	static class ClientBean {
		//스프링 부트 3.x 부터 
		//implementation 'jakarta.inject:jakarta.inject-api:2.0.1'
		private final Provider<PrototypeBean> prototypeBeanProvider; 
		
		public ClientBean(Provider<PrototypeBean> prototypeBeanProvider) {
			this.prototypeBeanProvider = prototypeBeanProvider;
		}
		
		public int logic() {
			PrototypeBean prototypeBean = prototypeBeanProvider.get();
			prototypeBean.addCount();
			int count = prototypeBean.getCount();
			return count;
		}
	}
	
	@Scope("prototype")
	static class PrototypeBean {
		private int count = 0;
		
		public void addCount() {
			count++;
		}
		
		public int getCount() {
			return count;
		}
		
		@PostConstruct
		public void init() {
			System.out.println("PrototypeBean.init " + this);
		}
		
		@PreDestroy
		public void destroy() {
			System.out.println("PrototypeBean.destroy");
		}
	}
}
