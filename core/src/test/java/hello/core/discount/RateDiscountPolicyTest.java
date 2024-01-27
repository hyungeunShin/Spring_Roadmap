package hello.core.discount;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicyTest {
	
	DiscountPolicy discountPolicy = new RateDiscountPolicy();
	
	@Test
	@DisplayName("VIP는 10% 할인")
	void vip_discount() {
		Member member = new Member(1L, "홍길동", Grade.VIP);
		
		int discount1 = discountPolicy.discount(member, 10000);
		int discount2 = discountPolicy.discount(member, 20000);
		
		assertEquals(1000, discount1);
		assertEquals(2000, discount2);
	}
	
	@Test
	@DisplayName("VIP가 아니면 할인은 없다")
	void basic_discount() {
		Member member = new Member(1L, "홍길동", Grade.BASIC);
		
		int discount1 = discountPolicy.discount(member, 10000);
		
		assertEquals(0, discount1);
	}
}
