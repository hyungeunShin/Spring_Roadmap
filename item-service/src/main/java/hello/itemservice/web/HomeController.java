package hello.itemservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final MemberRepository memberRepository;
	
	//@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/")
	public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
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
}