package hello.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

//구성정보
@Configuration
public class AppConfig {
	//스프링 컨테이너에 메소드 이름으로 등록
	@Bean
	public MemberService memberService() {
		System.out.println("AppConfig.memberService()");
		
		//MemoryMemberRepository 객체를 생성하고 그 값을 MemberServiceImpl에 생성자로 전달
		//MemberServiceImpl 입장에서 보면 의존관계를 마치 외부에서 주입해주는 것 같다고 해서
		//DI(Dependency Injection) 우리말로 의존성 주입이라 한다.
		
		//return new MemberServiceImpl(new MemoryMemberRepository());
		
		//나중에 수정사항으로 DB를 연결하기 위해 JdbcMemberRepository로 바꿔야 한다면 여기만 바꾸면 해결이 되기 때문에 메소드로 분리
		return new MemberServiceImpl(memberRepository());
	}
	
	@Bean
	OrderService orderService() {
		System.out.println("AppConfig.orderService()");
		
		//return new OrderServiceImpl(new MemoryMemberRepository(), new RateDiscountPolicy());
		return new OrderServiceImpl(memberRepository(), discountPolicy()); 
	}
	
	@Bean
	MemberRepository memberRepository() {
		System.out.println("AppConfig.memberRepository()");
		
		return new MemoryMemberRepository();
	}
	
	@Bean
	DiscountPolicy discountPolicy() {
		System.out.println("AppConfig.discountPolicy()");
		
		//return new FixDiscountPolicy();
		return new RateDiscountPolicy();
	}
}
