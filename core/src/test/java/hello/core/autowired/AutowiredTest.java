package hello.core.autowired;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.member.Member;
import jakarta.annotation.Nullable;

public class AutowiredTest {
	@Test
	void autowiredOption() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
		((AnnotationConfigApplicationContext) ac).close();
	}
	
	static class TestBean {
		//호출 자체가 안됨
		@Autowired(required = false)
		public void setNoBean1(Member noBean1) {
			System.out.println("noBean1 : " + noBean1);
		}
		
		//noBean2 : null
		@Autowired
		public void setNoBean2(@Nullable Member noBean2) {
			System.out.println("noBean2 : " + noBean2);
		}
		
		//noBean3 : Optional.empty
		@Autowired
		public void setNoBean3(Optional<Member> noBean3) {
			System.out.println("noBean3 : " + noBean3);
		}
	}
}
