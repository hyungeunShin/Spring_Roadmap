package hello.springmvc.basic.request;

import java.util.Locale;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RequestHeaderController {
	@RequestMapping("/headers")
	public String headers(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpMethod httpMethod,
			Locale locale,
			@RequestHeader MultiValueMap<String, String> headerMap,
			@RequestHeader("host") String host,
			@CookieValue(value = "myCookie", required = false) String cookie) {
		
		log.info("request = {}", request);
		log.info("response = {}", response);
		//HttpMethod : HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
		log.info("httpMethod = {}", httpMethod);
		log.info("locale = {}", locale);
		//@RequestHeader MultiValueMap<String, String> headerMap, 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
		log.info("headerMap = {}", headerMap);
		log.info("header host = {}", host);
		log.info("myCookie = {}", cookie);
		
		return "ok";
	}
}
