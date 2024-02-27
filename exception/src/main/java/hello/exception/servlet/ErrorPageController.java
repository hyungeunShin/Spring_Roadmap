package hello.exception.servlet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ErrorPageController {
	//RequestDispatcher 상수로 정의되어 있음
	public static final String ERROR_EXCEPTION 		= "jakarta.servlet.error.exception";
	public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
	public static final String ERROR_MESSAGE 		= "jakarta.servlet.error.message";
	public static final String ERROR_REQUEST_URI 	= "jakarta.servlet.error.request_uri";
	public static final String ERROR_SERVLET_NAME 	= "jakarta.servlet.error.servlet_name";
	public static final String ERROR_STATUS_CODE 	= "jakarta.servlet.error.status_code";

	@RequestMapping("/error-page/404")
	public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 404");
		errorInfo(request);
		return "error-page/404";
	}

	@RequestMapping("/error-page/500")
	public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
		log.info("errorPage 500");
		errorInfo(request);
		return "error-page/500";
	}
	
	@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> errorPage500API(HttpServletRequest request, HttpServletResponse response) {
		log.info("API error 500");
		
		Map<String, Object> result = new HashMap<>();
		Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
		
		result.put("status", request.getAttribute(ERROR_STATUS_CODE));
		result.put("message", ex.getMessage());
		
		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		return new ResponseEntity<Map<String,Object>>(result, HttpStatus.valueOf(statusCode));
	}
	
	private void errorInfo(HttpServletRequest request) {
		log.info("ERROR_EXCEPTION = {}", request.getAttribute(ERROR_EXCEPTION));
		log.info("ERROR_EXCEPTION_TYPE = {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
		log.info("ERROR_MESSAGE = {}", request.getAttribute(ERROR_MESSAGE));
		log.info("ERROR_REQUEST_URI = {}", request.getAttribute(ERROR_REQUEST_URI));
		log.info("ERROR_SERVLET_NAME = {}", request.getAttribute(ERROR_SERVLET_NAME));
		log.info("ERROR_STATUS_CODE = {}", request.getAttribute(ERROR_STATUS_CODE));
		
		/*
		오류가 발생하면 오류 페이지를 출력하기 위해 WAS 내부에서 다시 한번 호출이 발생한다. 이때 필터, 서블릿, 인터셉터도 모두 다시 호출된다.
		그런데 로그인 인증 체크 같은 경우를 생각해보면 이미 한번 필터나 인터셉터에서 로그인 체크를 완료했다.
		따라서 서버 내부에서 오류 페이지를 호출한다고 해서 해당 필터나 인터셉트가 한번 더 호출되는 것은 매우 비효율적이다.
		결국 클라이언트로 부터 발생한 정상 요청인지 아니면 오류 페이지를 출력하기 위한 내부 요청인지 구분할 수 있어야 한다.
		서블릿은 이런 문제를 해결하기 위해 DispatcherType 이라는 추가 정보를 제공한다.
		*/
		log.info("dispatchType = {}", request.getDispatcherType());
	}
}