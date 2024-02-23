package hello.itemservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import hello.itemservice.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final MemberRepository memberRepository;
	
	private final SessionManager sessionManager;
	
	//@GetMapping("/")
	public String home() {
		return "home";
	}
	
	//@GetMapping("/")
	public String homeLoginV1(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
		if(memberId == null) {
			return "home";
		}
		
		Member member = memberRepository.findById(memberId);
		if(member == null) {
			return "home";
		}
		
		model.addAttribute("member", member);
		return "loginHome";
	}
	
	//@GetMapping("/")
	public String homeLoginV2(HttpServletRequest request, Model model) {
		Member member = (Member) sessionManager.getSession(request);
		
		if(member == null) {
			return "home";
		}
		
		model.addAttribute("member", member);
		return "loginHome";
	}
	
	//@GetMapping("/")
	public String homeLoginV3(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return "home";
		}
		
		Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
		if(member == null) {
			return "home";
		}
		
		model.addAttribute("member", member);
		return "loginHome";
	}
	
	@GetMapping("/")
	public String homeLoginV4(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {
		if(member == null) {
			return "home";
		}
		
		model.addAttribute("member", member);
		return "loginHome";
	}
}
