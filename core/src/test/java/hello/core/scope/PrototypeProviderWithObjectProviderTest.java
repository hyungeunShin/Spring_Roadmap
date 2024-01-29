package hello.core.scope;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class PrototypeProviderWithObjectProviderTest {
	@Test
	void providerTest() {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
		
		//PrototypeBean.init hello.core.scope.PrototypeProviderWithObjectProviderTest$PrototypeBean@79351f41
		//PrototypeBean.init hello.core.scope.PrototypeProviderWithObjectProviderTest$PrototypeBean@53bd8fca
		
		ClientBean clientBean1 = ac.getBean(ClientBean.class);
		int count1 = clientBean1.logic();
		assertEquals(1, count1);
		
		ClientBean clientBean2 = ac.getBean(ClientBean.class);
		int count2 = clientBean2.logic();
		assertEquals(1, count2);
		
		ac.close();
	}
	
	@Scope("singleton")
	static class ClientBean {
		private final ObjectProvider<PrototypeBean> prototypeBeanProvider;
		
		public ClientBean(ObjectProvider<PrototypeBean> prototypeBeanProvider) {
			this.prototypeBeanProvider = prototypeBeanProvider;
		}
		
		public int logic() {
			PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
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
