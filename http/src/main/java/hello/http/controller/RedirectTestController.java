package hello.http.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.http.domain.Member;
import hello.http.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RedirectTestController {
	private final MemberService memberService;
	
	@GetMapping("/test")
	public String testView() {
		return "test";
	}
	
	@PostMapping("/test")
	public ResponseEntity<Object> test(@ModelAttribute Member member) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		
		//headers.setLocation(new URI("/test1"));
		//return new ResponseEntity<Object>("", headers, HttpStatus.TEMPORARY_REDIRECT);
		
		//headers.setLocation(new URI("/test1?name=" + member.getName() + "&age=" + member.getAge()));
		//return new ResponseEntity<Object>("", headers, HttpStatus.SEE_OTHER);
		
		memberService.join(member);
		headers.setLocation(new URI("/test1/" + member.getId()));
		return new ResponseEntity<Object>(null, headers, HttpStatus.FOUND);
	}
	
	@GetMapping("/test1/{id}")
	public String redirectGet(@PathVariable(name = "id") Long id, Model model) throws Exception {
		Member member = memberService.findOne(id);
		model.addAttribute("member", member);
		return "result";
	}
	
	@PostMapping("/test1")
	@ResponseBody
	public String redirectPost(@ModelAttribute Member member) {
		System.out.println("redirectPost : " + member.toString());
		return member.toString();
	}
}
