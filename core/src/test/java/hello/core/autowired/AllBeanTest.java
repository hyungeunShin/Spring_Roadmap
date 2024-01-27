package hello.core.autowired;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.discount.DiscountPolicy;

public class AllBeanTest {
	@Test
	void findAllBean() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(DiscountService.class);
	}
	
	static class DiscountService {
		private final Map<String, DiscountPolicy> policyMap;
		private final List<DiscountPolicy> policies;
		
		public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
			this.policyMap = policyMap;
			this.policies = policies;
		}
	}
}
