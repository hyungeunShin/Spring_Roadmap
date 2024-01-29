package hello.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderService;

public class OrderApp {
	/*
	public static void main(String[] args) {
		//MemberService memberService = new MemberServiceImpl();
		//OrderService orderService = new OrderServiceImpl();
		
		//AppConfig ac = new AppConfig();
		//MemberService memberService = ac.memberService();
		//OrderService orderService = ac.orderService();
		
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		MemberService memberService = ac.getBean("memberService", MemberService.class);
		OrderService orderService = ac.getBean("orderService", OrderService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		Long memberId = 1L;
		Member member = new Member(memberId, "홍길동", Grade.VIP);
		memberService.join(member);
		
		Order order = orderService.createOrder(memberId, "아이템", 20000);
		
		System.out.println("Order : " + order);
		System.out.println("Order.calculatePrice : " + order.calculatePrice());
	}
	*/
}
