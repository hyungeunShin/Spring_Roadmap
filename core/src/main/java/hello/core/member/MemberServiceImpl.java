package hello.core.member;

import org.springframework.stereotype.Component;

import hello.core.annotation.MainMemberRepository;

@Component
//@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	/*
	DIP, OCP 위반
	인터페이스 뿐만 아니라 구현 클래스에도 의존한다
	기능을 확장해서 변경하면 코드에 영향을 준다
	
	private final MemberRepository memberRepository = new MemoryMemberRepository();
	
	그래서 인터페이스에만 의존하게 변경 하지만 구현체가 없기 때문에 NullPointerException 발생
	관심사의 분리(책임을 분리한다) ==> AppConfig의 등장 : 어플리케이션의 전체 동작 방식을 구성하기 위해 구현 객체를 생성하고, 연결하는 책임을 가진 별도의 설정 클래스
	*/
	
	//DIP 원칙을 준수 => 오로지 인터페이스에만 의존
	//MemberServiceImpl 입장에서는 어떤 구현 클래스가 들어올지 알 수 없다.
	
	private final MemberRepository memberRepository;
	
	//생성자가 하나면 @Autowired 생략 가능
	//@Autowired	//applicationContext.getBean(MemberRepository.class)
	public MemberServiceImpl(@MainMemberRepository MemberRepository memberRepository) {
		System.out.println("memberServiceImpl 생성자 : " + memberRepository);
		this.memberRepository = memberRepository;
	}

	@Override
	public void join(Member member) {
		memberRepository.save(member);
	}

	@Override
	public Member findMember(Long memberId) {
		return memberRepository.findById(memberId);
	}

	public MemberRepository getMemberRepository() {
		return memberRepository;
	}
}
