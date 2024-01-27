package hello.core.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;

public class OrderServiceTest {
	private MemberService memberService;
	private OrderService orderService;
	
	@BeforeEach
	void before() {
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		memberService = ac.getBean("memberService", MemberService.class);
		orderService = ac.getBean("orderService", OrderService.class);
		((AnnotationConfigApplicationContext) ac).close();
	}
	
	@Test
	@DisplayName("주문")
	void createOrder() {
		Long memberId = 1L;
		Member member = new Member(memberId, "홍길동", Grade.VIP);
		memberService.join(member);
		
		Order order = orderService.createOrder(memberId, "아이템", 10000);
		
		assertEquals(1000, order.getDiscountPrice());
		assertEquals(order.calculatePrice(), order.getItemPrice() - order.getDiscountPrice());
	}
}
