package hello.core.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import hello.core.annotation.MainMemberRepository;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	/*
	DIP, OCP 위반
	인터페이스 뿐만 아니라 구현 클래스에도 의존한다
	기능을 확장해서 변경하면 코드에 영향을 준다
	
	private final MemberRepository memberRepository = new MemoryMemberRepository();
	
	//private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
	private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
	
	그래서 인터페이스에만 의존하게 변경 => 하지만 구현체가 없기 때문에 NullPointerException 발생
	관심사의 분리(책임을 분리한다) ==> AppConfig의 등장 : 어플리케이션의 전체 동작 방식을 구성하기 위해 구현 객체를 생성하고, 연결하는 책임을 가진 별도의 설정 클래스
	*/
	
	//DIP 원칙을 준수 => 오로지 인터페이스에만 의존
	//OrderServiceImpl 입장에서는 어떤 구현 클래스가 들어올지 알 수 없다.
	private final MemberRepository memberRepository;
	
	private final DiscountPolicy discountPolicy;
	
	//생성자가 하나면 @Autowired 생략 가능
	//@Autowired
	public OrderServiceImpl(@MainMemberRepository MemberRepository memberRepository, @Qualifier("rateDiscountPolicy") DiscountPolicy discountPolicy) {
		this.memberRepository = memberRepository;
		this.discountPolicy = discountPolicy;
	}

	@Override
	public Order createOrder(Long memberId, String itemName, int itemPrice) {
		Member member = memberRepository.findById(memberId);
		
		//단일 책임의 원칙
		//OrderService 입장에서는 할인에 대해서는 잘 모른다.
		//discountPolicy를 통해 결과만 받는다.
		int discount = discountPolicy.discount(member, itemPrice);
		
		return new Order(memberId, itemName, itemPrice, discount);
	}

	public MemberRepository getMemberRepository() {
		return memberRepository;
	}
}
