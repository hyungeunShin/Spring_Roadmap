package hello.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;

public class MemberApp {
	/*
	public static void main(String[] args) {
		//OSP, DIP 안지켜짐
		//MemberService memberService = new MemberServiceImpl();
		
		//AppConfig ac = new AppConfig();
		//MemberService memberService = ac.memberService();
		
		ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		MemberService memberService = ac.getBean("memberService", MemberService.class);
		((AnnotationConfigApplicationContext) ac).close();
		
		Member member = new Member(1L, "홍길동", Grade.VIP);
		memberService.join(member);
		
		Member findMember = memberService.findMember(member.getId());
		System.out.println("member : " + member.toString());
		System.out.println("findMember : " + findMember.toString());
	}
	*/
}
