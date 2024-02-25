package hello.itemservice.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.itemservice.domain.login.LoginService;
import hello.itemservice.domain.member.Member;
import hello.itemservice.web.SessionConst;
import hello.itemservice.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	
	private final SessionManager sessionManager;
	
	@GetMapping("/login")
	public String loginForm(@ModelAttribute LoginForm form) {
		return "login/loginForm";
	}
	
	//@PostMapping("/login")
	public String loginV1(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
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
		/*
		보안 문제
			쿠키 값은 임의로 변경할 수 있다.
			쿠키에 보관된 정보는 훔쳐갈 수 있다.
		대안
			- 쿠키에 중요한 값을 노출하지 않고, 사용자 별로 예측 불가능한 임의의 토큰(랜덤 값)을 노출하고, 서버에서 토큰과 사용자 id를 매핑해서 인식한다. 그리고 서버에서 토큰을 관리한다.
			- 토큰은 해커가 임의의 값을 넣어도 찾을 수 없도록 예상 불가능 해야 한다.
			- 해커가 토큰을 털어가도 시간이 지나면 사용할 수 없도록 서버에서 해당 토큰의 만료시간을 짧게(예: 30분) 유지한다. 또는 해킹이 의심되는 경우 서버에서 해당 토큰을 강제로 제거하면 된다. 
		*/
		Cookie cookie = new Cookie("memberId", String.valueOf(member.getId()));
		response.addCookie(cookie);
		
		return "redirect:/";
	}
	
	//@PostMapping("/login")
	public String loginV2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
		log.info("LoginForm = {}", form);
		
		if(bindingResult.hasErrors()) {
			return "login/loginForm"; 
		}
		
		Member member = loginService.login(form.getLoginId(), form.getPassword());
		
		if(member == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		//쿠키 값을 변조 가능 -> 예상 불가능한 복잡한 세션Id를 사용한다.
		//쿠키에 보관하는 정보는 클라이언트 해킹시 털릴 가능성이 있다. -> 세션Id가 털려도 여기에는 중요한 정보가 없다.
		sessionManager.createSession(member, response);
		
		return "redirect:/";
	}
	
	//@PostMapping("/login")
	public String loginV3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
		log.info("LoginForm = {}", form);
		
		if(bindingResult.hasErrors()) {
			return "login/loginForm"; 
		}
		
		Member member = loginService.login(form.getLoginId(), form.getPassword());
		
		if(member == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		/*
		서블릿이 제공하는 HttpSession 도 결국 우리가 직접 만든 SessionManager 와 같은 방식으로 동작한다.
		서블릿을 통해 HttpSession 을 생성하면 다음과 같은 쿠키를 생성한다.
		쿠키 이름이 JSESSIONID 이고, 값은 추정	불가능한 랜덤 값이다.
		
		request.getSession(true)
			- 세션이 있으면 기존 세션을 반환한다.
			- 세션이 없으면 새로운 세션을 생성해서 반환한다.
		request.getSession(false)
			- 세션이 있으면 기존 세션을 반환한다.
			- 세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.
		request.getSession() : 신규 세션을 생성하는 request.getSession(true) 와 동일하다.
		*/
		//세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER, member);
		
		return "redirect:/";
	}
	
	@PostMapping("/login")
	public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult
			, @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL
			, HttpServletRequest request) {
		
		log.info("LoginForm = {}", form);
		
		if(bindingResult.hasErrors()) {
			return "login/loginForm"; 
		}
		
		Member member = loginService.login(form.getLoginId(), form.getPassword());
		
		if(member == null) {
			bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		HttpSession session = request.getSession();
		session.setAttribute(SessionConst.LOGIN_MEMBER, member);
		
		return "redirect:" + redirectURL;
	}
	
	//@PostMapping("/logout")
	public String logoutV1(HttpServletResponse response) {
		expireCookie(response, "memberId");
		
		return "redirect:/";
	}
	
	//@PostMapping("/logout")
	public String logoutV2(HttpServletRequest request) {
		sessionManager.expire(request);
		
		return "redirect:/";
	}
	
	@PostMapping("/logout")
	public String logoutV3(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			session.invalidate();
		}
		
		return "redirect:/";
	}

	private void expireCookie(HttpServletResponse response, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
