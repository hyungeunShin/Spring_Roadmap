package hello.http.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CookieController {
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> postLogin(
			@RequestParam(name = "username") String username
			, @RequestParam(name = "password") String password
			, HttpServletResponse response) {
		System.out.println("아이디 : " + username);
		System.out.println("비밀번호 : " + password);
		
		Cookie cookie = new Cookie("user", username);
		cookie.setPath("/");
		cookie.setMaxAge(24*60*60);
		cookie.setDomain("localhost");
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		response.addCookie(cookie);
		
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}
}
