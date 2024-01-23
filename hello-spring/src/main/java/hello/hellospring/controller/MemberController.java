package hello.hellospring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;

@Controller
public class MemberController {
	/*
	 스프링 빈을 등록하는 2가지 방법
	 	1. 컴포넌트 스캔과 자동 의존관계 설정
	 		- @Component 가 있으면 스프링 빈으로 자동 등록된다.
	 		- @Component를 포함하는 다음 Annotation 도 스프링 빈으로 자동 등록
	 			1) @Controller
	 			2) @Service
	 			3) @Repository
	 			
	 	2. 자바 코드로 직접 스프링 빈 등록하기
	 		- SpringConfig.java 참고
	 	
	 ※ 스프링 컨테이너에 스프링 빈을 등록할 때, 기본적으로 싱글톤 패턴 사용
	 	
	 ※ 그러면 아무곳에나 @Component를 붙이면 스프링 빈이 되는가?
	 	SpringBoot1Application.java의 패키지 = com.example.SpringBoot1
	 	com.example.SpringBoot1 하위는 자동으로 등록된다
	 	
	 	하지만 com.example.SpringBoot1와 동일하거나 하위 패키지가 아닌 애들은 스프링 빈으로 컴포넌트 스캔을 하지 않는다.
	 	설정을 통해 가능하지만 기본은 스캔 대상이 아니다!
	 
	 ※ DI(Dependency Injection)에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지가 있다.
	   의존관계가 실행중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입을 권장한다.
	   ★★★ 생성자 위에 @Autowired를 붙이지만 생성자가 딱 하나만 있으면 스프링 빈으로 등록되기 때문에 @Autowired는 생략 가능하다 
	 
	 ※ 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.
	 */
	
	// 필드 주입
	//@Autowired
	//private final MemberService memberService;
	
	
	//setter 주입
	//private MemberService memberService;
	
	//@Autowired
	//public void setMemberService(MemberService memberService) {
	//	this.memberService = memberService;
	//}
	
	
	//생성자 주입
	private final MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
		
		//AOP가 적용이 되면 MemberService를 복제해서 조작
		//class com.example.SpringBoot1.service.MemberService$$SpringCGLIB$$0
		System.out.println("memberService : " + memberService.getClass());
	}
	
	@GetMapping("/members/new")
	public String createForm() {
		return "members/createMemberForm";
	}
	
	@PostMapping("/members/new")
	public String create(MemberForm form) {
		Member member = new Member();
		member.setName(form.getName());
		
		memberService.join(member);
		
		return "redirect:/";
	}
	
	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers();
		model.addAttribute("members", members);
		
		return "members/memberList";
	}
}
