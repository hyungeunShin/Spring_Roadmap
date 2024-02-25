package hello.itemservice.web.interceptor;

import java.util.UUID;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
	/*
	스프링 인터셉터 흐름
		HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러
		
	스프링 인터셉터 체인
		HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러
		
		스프링 인터셉터는 체인으로 구성되는데, 중간에 인터셉터를 자유롭게 추가할 수 있다.
		예를 들어서 로그를 남기는 인터셉터를 먼저 적용하고 그 다음에 로그인 여부를 체크하는 인터셉터를 만들 수 있다.
	*/
	
	public static final String LOG_ID = "logId";
	
	/*
	preHandle : 컨트롤러 호출 전에 호출된다. (더 정확히는 핸들러 어댑터 호출 전에 호출된다.)
	preHandle 의 응답값이 true 이면 다음으로 진행하고, false 이면 더는 진행하지 않는다.
	false 인 경우 나머지 인터셉터는 물론이고 핸들러 어댑터도 호출되지 않는다.
	예외가 발생하면 컨트롤러 호출 전에 호출된다.
	*/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();
		request.setAttribute(LOG_ID, uuid);
		
		//@RequestMapping : HandlerMethod
		//정적 리소스 : ResourceHttpRequestHandler
		if(handler instanceof HandlerMethod) {
			//호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
			HandlerMethod hm = (HandlerMethod) handler;
			log.info("HandlerMethod [{}]", hm);
		}
		
		log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
		
		//false 일 땐 진행X
		return true;	//다음 진행
	}

	/*
	postHandle : 컨트롤러 호출 후에 호출된다. (더 정확히는 핸들러 어댑터 호출 후에 호출된다.)
	컨트롤러에서 예외가 발생하면 postHandle 은 호출되지 않는다. 
	*/
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.info("postHandle [{}]", modelAndView);
	}
	
	/*
	afterCompletion : 뷰가 렌더링 된 이후에 호출된다.
	afterCompletion 은 항상 호출된다. 이 경우 예외(ex)를 파라미터로 받아서 어떤 예외가 발생했는지 로그로 출력할 수 있다.
	*/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String logId = (String) request.getAttribute(LOG_ID);
		
		log.info("RESPONSE [{}][{}][{}]", logId, request.getRequestURI(), handler);
		
		if(ex != null) {
			log.error("afterCompletion 에러 {}", ex);
		}
	}
}
