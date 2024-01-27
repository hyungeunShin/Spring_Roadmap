package hello.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 비즈니스 요구사항과 설계
	- 회원
		- 회원을 가입하고 조회할 수 있다.
		- 회원은 일반과 VIP 두 가지 등급이 있다.
		- 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다. (미확정)
	- 주문과 할인 정책
		- 회원은 상품을 주문할 수 있다.
		- 회원 등급에 따라 할인 정책을 적용할 수 있다.
		- 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용해달라. (나중에 변경 될 수 있다.)
		- 할인 정책은 변경 가능성이 높다. 회사의 기본 할인 정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수 도 있다. (미확정)
		
		요구사항을 보면 회원 데이터, 할인 정책 같은 부분은 지금 결정하기 어려운 부분이다. 
		그렇다고 이런 정책이 결정될 때까지 개발을 무기한 기다릴 수 도 없다.
		인터페이스를 만들고 구현체를 언제든지 갈아끼울 수 있도록 설계하면 된다.
 */

@SpringBootApplication
public class CoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}
}
