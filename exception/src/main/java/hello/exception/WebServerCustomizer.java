package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component	//스프링 부트가 제공하는 기본 오류 메커니즘(BasicErrorController)을 사용하도록 주석처리(이러면 오류가 발생했을 때 오류 페이지로 /error 를 기본 요청)
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	/*
	//웹 페이지 예외
	ErrorPage 를 자동으로 등록한다. 이때 /error 라는 경로로 기본 오류 페이지를 설정한다.
		- new ErrorPage("/error"), 상태코드와 예외를 설정하지 않으면 기본 오류 페이지로 사용된다.
		- 서블릿 밖으로 예외가 발생하거나 response.sendError(...) 가 호출되면 모든 오류는 /error 를 호출하게 된다. 
	BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록한다.
		- ErrorPage 에서 등록한 /error 를 매핑해서 처리하는 컨트롤러다.
	
	※ 참고 ErrorMvcAutoConfiguration 이라는 클래스가 오류 페이지를 자동으로 등록하는 역할을 한다.
	
	뷰 템플릿이 정적 리소스보다 우선순위가 높고 404, 500처럼 구체적인 것이 5xx처럼 덜 구체적인 것 보다 우선순위가 높다.
	5xx, 4xx 라고 하면 500대, 400대 오류를 처리해준다.(resources/templates/error/ooo.html)
	적용 대상이 없을 때는 뷰 이름(error) resources/templates/error.html 이다.
	*/
	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
		ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
		
		ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
		
		factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
	}
	
	/*
	API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 한다.
	
	BasicErrorController를 보면
		@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
		public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {}
		
		@RequestMapping
		public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {}
		
		errorHtml() : produces = MediaType.TEXT_HTML_VALUE : 클라이언트 요청의 Accept 해더 값이 text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공한다.
		error() : 그 외 경우에 호출되고 ResponseEntity 로 HTTP Body 에 JSON 데이터를 반환한다.
	*/
}