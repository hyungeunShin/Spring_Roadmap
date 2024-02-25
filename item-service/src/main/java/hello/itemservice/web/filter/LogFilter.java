package hello.itemservice.web.filter;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ServletComponentScan @WebFilter(filterName = "logFilter", urlPatterns = "/*") 로 필터 등록이 가능하지만 필터 순서 조절이 안된다.
//WebConfig.class처럼 FilterRegistrationBean을 사용하는게 좋다.
public class LogFilter implements Filter {
	/*
	필터는 서블릿이 지원하는 수문장 역할이다.
	
	필터 흐름
		HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러
		
		필터를 적용하면 필터가 호출 된 다음에 서블릿이 호출된다. 그래서 모든 고객의 요청 로그를 남기는 요구사항이 있다면 필터를 사용하면 된다.
		참고로 필터는 특정 URL 패턴에 적용할 수 있다. /* 이라고 하면 모든 요청에 필터가 적용된다.
		참고로 스프링을 사용하는 경우 여기서 말하는 서블릿은 스프링의 디스패처 서블릿으로 생각하면 된다.
	*/
	
	//필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출된다.
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("log filter init");
	}
	
	//고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*
		필터 체인
			HTTP 요청 -> WAS -> 필터1 -> 필터2 -> 필터3 -> 서블릿 -> 컨트롤러
			필터는 체인으로 구성되는데 중간에 필터를 자유롭게 추가할 수 있다.
			예를 들어서 로그를 남기는 필터를 먼저 적용하고 그 다음에 로그인 여부를 체크하는 필터를 만들 수 있다.
		*/
		
		log.info("log filter doFilter");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();
		
		String uuid = UUID.randomUUID().toString();
		
		try {
			log.info("REQUEST [{}][{}]", uuid, requestURI);
			
			/*
			다음 필터가 있으면 필터를 호출하고, 필터가 없으면 서블릿을 호출한다.
			만약 이 로직을 호출하지 않으면 다음 단계로 진행되지 않는다.
			
			chain.doFilter(request, response); 를 호출해서 다음 필터 또는 서블릿을 호출할 때 request, response 를 다른 객체로 바꿀 수 있다.
			ServletRequest, ServletResponse 를 구현한 다른 객체를 만들어서 넘기면 해당 객체가 다음 필터 또는 서블릿에서 사용된다.
			*/
			chain.doFilter(request, response);
		} catch(Exception e) {
			throw e;
		} finally {
			log.info("RESPONSE [{}][{}]", uuid, requestURI);
		}
	}
	
	//필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.
	@Override
	public void destroy() {
		log.info("log filter destroy");
	}
}
