package hello.servlet.web.springmvc.v1;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

@Controller
public class SpringMemberListControllerV1 {
	@RequestMapping("/springmvc/v1/members")
	public ModelAndView process() {
		MemberRepository memberRepository = MemberRepository.getInstance();
		
		List<Member> members = memberRepository.findAll();
		
		ModelAndView mv = new ModelAndView("members");
		mv.addObject("members", members);
		return mv;
	}
}
