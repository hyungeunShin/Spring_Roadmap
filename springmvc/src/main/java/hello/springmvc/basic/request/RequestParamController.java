package hello.springmvc.basic.request;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RequestParamController {
	@RequestMapping("/request-param-v1")
	public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		int age = Integer.parseInt(request.getParameter("age"));

		log.info("username = {}, age = {}", username, age);

		response.getWriter().write("ok");
	}

	@ResponseBody
	@RequestMapping("/request-param-v2")
	public String requestParamV2(@RequestParam("username") String username, @RequestParam("age") int age) {
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	//스프링 부트 3.2부터 자바 컴파일러에 -parameters 옵션을 넣어주어야 애노테이션에 적는 이름을 생략할 수 있다.
	//스프링 부트 3.2 전까지는 바이트코드를 파싱해서 매개변수 이름을 추론하려고 시도했다. 하지만 스프링 부트 3.2부터는 이런 시도를 하지 않는다.
	/*
	@ResponseBody
	@RequestMapping("/request-param-v3")
	public String requestParamV3(@RequestParam String username, @RequestParam int age) {
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/request-param-v4")
	public String requestParamV4(String username, int age) {
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	*/
	
	@ResponseBody
	@RequestMapping("/request-param-v3")
	public String requestParamV3(@RequestParam("username") String username, @RequestParam("age") int age) {
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/request-param-v4")
	public String requestParamV4(@RequestParam("username") String username, @RequestParam("age") int age) {
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/request-param-required")
	public String requestParamRequired(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "age", required = false) Integer age) {
		
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/request-param-default")
	public String requestParamDefault(
			@RequestParam(value = "username", required = true, defaultValue = "guest") String username,
			@RequestParam(value = "age", required = false, defaultValue = "-1") int age) {
		
		//defaultValue는 빈 문자의 경우에도 적용 
		//예) /request-param-default?username=
		log.info("username = {}, age = {}", username, age);
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/request-param-map")
	public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
		//MultiValueMap 가능
		log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/model-attribute-v1")
	public String modelAttributeV1(@ModelAttribute HelloData helloData) {
		log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
		return "ok";
	}
	
	@ResponseBody
	@RequestMapping("/model-attribute-v2")
	public String modelAttributeV2(HelloData helloData) {
		log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
		return "ok";
	}
}
