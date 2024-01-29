package hello.core.autowired;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;

public class AllBeanTest {
	@Test
	void findAllBean() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
		DiscountService discountService = ac.getBean(DiscountService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		Member member = new Member(1L, "홍길동", Grade.VIP);
		int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");
		
		assertThat(discountService).isInstanceOf(DiscountService.class);
		assertEquals(1000, discountPrice);
		
		int discountPrice2 = discountService.discount(member, 20000, "rateDiscountPolicy");
		assertEquals(2000, discountPrice2);
	}
	
	static class DiscountService {
		//스프링은 DiscountPolicy의 구현체들을 모두 찾아서, 그 구현체의 이름(빈 이름)을 Key로, 구현 객체를 Value로 해서 Map에 담아서 주입
		private final Map<String, DiscountPolicy> policyMap;
		//List 타입인 List<DiscountPolicy>에는 DiscountPolicy의 구현체들을 List에 담아서 주입
		private final List<DiscountPolicy> policies;
		
		@Autowired
		public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
			this.policyMap = policyMap;
			this.policies = policies;
			
			//policyMap : {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@532721fd, rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@410954b}
			//policies  : [hello.core.discount.FixDiscountPolicy@532721fd, hello.core.discount.RateDiscountPolicy@410954b]
			System.out.println("policyMap : " + policyMap);
			System.out.println("policies  : " + policies);
		}

		public int discount(Member member, int price, String discountCode) {
			DiscountPolicy discountPolicy = policyMap.get(discountCode);
			return discountPolicy.discount(member, price);
		}
	}
}
