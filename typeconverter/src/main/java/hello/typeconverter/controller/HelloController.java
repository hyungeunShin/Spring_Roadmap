package hello.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HelloController {
	/*
	스프링은 확장 가능한 컨버터 인터페이스를 제공한다.
	개발자는 스프링에 추가적인 타입 변환이 필요하면 이 컨버터 인터페이스를 구현해서 등록하면 된다.
	이 컨버터 인터페이스는 모든 타입에 적용할 수 있다. 
	필요하면 X -> Y 타입으로 변환하는 컨버터 인터페이스를 만들고 또 Y -> X 타입으로 변환하는 컨버터 인터페이스를 만들어서 등록하면 된다.
	예를 들어서 문자로 "true" 가 오면 Boolean 타입으로 받고 싶으면 String -> Boolean 타입으로 변환되도록 컨버터 인터페이스를 만들어서 등록하고,
	반대로 적용하고 싶으면 Boolean -> String 타입으로 변환되도록 컨버터를 추가로 만들어서 등록하면 된다. 
	*/
	
	@GetMapping("/hello-v1")
	public String helloV1(HttpServletRequest request) {
		//HTTP 요청 파라미터는 모두 문자로 처리된다.
		//따라서 요청 파라미터를 자바에서 다른 타입으로 변환해서 사용하고 싶으면 다음과 같이 숫자 타입으로 변환하는 과정을 거쳐야 한다.
		String data = request.getParameter("data");
		Integer intValue = Integer.valueOf(data);
		log.info("data : {}", intValue);
		return "ok";
	}
	
	@GetMapping("/hello-v2")
	public String helloV2(@RequestParam("data") Integer data) {
		//스프링이 중간에서 타입을 변환
		//@ModelAttribute, @PathVariable 에서도 확인 가능
		
		/*
		스프링의 타입 변환 적용 예
			- 스프링 MVC 요청 파라미터
				@RequestParam, @ModelAttribute, @PathVariable
			- @Value 등으로 YML 정보 읽기
			- XML에 넣은 스프링 빈 정보를 변환
			- 뷰를 렌더링 할 때 
		*/
		
		log.info("data : {}", data);
		return "ok";
	}
	
	//포맷터 적용 후 http://localhost/hello-v2?data=10,000
	@GetMapping("/hello-v3")
	public String helloV3(@RequestParam("data") Long data) {
		log.info("data : {}", data);
		return "ok";
	}
	
	//@RequestParam 은 @RequestParam 을 처리하는 ArgumentResolver 인 RequestParamMethodArgumentResolver 에서 ConversionService 를 사용해서 타입을 변환
	@GetMapping("/ip-port")
	public String ipPort(@RequestParam("ipPort") IpPort ipPort) {
		//http://localhost/ip-port?ipPort=127.0.0.1:80
		
		//ipPort.ip : 127.0.0.1
		log.info("ipPort.ip : {}", ipPort.getIp());
		
		//ipPort.port : 80
		log.info("ipPort.port : {}", ipPort.getPort());
		
		return "ok";
	}
}
