package hello.itemservice.web.filter;

import java.io.IOException;

import org.springframework.util.PatternMatchUtils;

import hello.itemservice.web.SessionConst;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckFilter implements Filter {
	private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		try {
			log.info("인증 체크 필터 시작 {}", requestURI);
			
			if(isLoginCheckPath(requestURI)) {
				log.info("인증 체크 로직 실행");
				
				HttpSession session = httpRequest.getSession(false);
				if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
					log.info("미인증 사용자 요청");
					httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
					
					//필터를 더는 진행하지 않는다. 미인증 사용자는 다음으로 진행하지 않고 끝
					//앞서 redirect 를 사용했기 때문에 redirect 가 응답으로 적용되고 요청이 끝난다.
					return;
				}
			}
			
			chain.doFilter(request, response);
		} catch(Exception e) {
			throw e;
		} finally {
			log.info("인증 체크 필터 종료");
		}
	}
	
	//화이트 리스트의 경우 인증 체크 X
	private boolean isLoginCheckPath(String requestURI) {
		return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
	}
}
