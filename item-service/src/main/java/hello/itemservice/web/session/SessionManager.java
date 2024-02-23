package hello.itemservice.web.session;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SessionManager {
	public static final String SESSION_COOKIE_NAME = "mySessionId";
	
	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
	
	//세션 생성
	public void createSession(Object value, HttpServletResponse response) {
		//sessionId 생성(추정 불가능한 랜덤 값)
		String sessionId = UUID.randomUUID().toString();
		
		//세션 저장소에 sessionId와 보관할 값 저장
		sessionStore.put(sessionId, value);
		
		//sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		response.addCookie(cookie);
	}
	
	//세션 조회
	public Object getSession(HttpServletRequest request) {
		Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
		
		if(cookie == null) {
			return null;
		}
		
		return sessionStore.get(cookie.getValue());
	}
	
	public Cookie findCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		
		if(cookies == null) {
			return null;
		}
		
		return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findAny()
				.orElse(null);
	}
	
	//세션 만료
	public void expire(HttpServletRequest request) {
		Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
		
		if(cookie != null) {
			sessionStore.remove(cookie.getValue());
		}
	}
}
