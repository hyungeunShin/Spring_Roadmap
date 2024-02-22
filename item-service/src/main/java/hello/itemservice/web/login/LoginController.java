package hello.itemservice.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	
	@GetMapping("/login")
	public String loginForm(@ModelAttribute LoginForm form) {
		return "login/loginForm";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		log.info("LoginForm = {}", form);
		
		if(bindingResult.hasErrors()) {
			return "login/loginForm"; 
		}
		
		Member member = loginService.login(form.getLoginId(), form.getPassword());
		
		if(member == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		//영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
		//세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지
		Cookie cookie = new Cookie("memberId", String.valueOf(member.getId()));
		response.addCookie(cookie);
		
		return "redirect:/";
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		expireCookie(response, "memberId");
		
		return "redirect:/";
	}

	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
