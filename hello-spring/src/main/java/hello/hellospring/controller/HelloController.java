package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {
	@GetMapping("/hello")
	public String hello(Model model) {
		model.addAttribute("data", "hello");
		
		//스프링 부트 템플릿엔진 기본 viewName 매핑 = resources:templates/{viewName}.html
		return "hello";
	}
	
	@GetMapping("/hello-mvc")
	public String helloMvc(@RequestParam(value="name", required=false) String name, Model model) {
		model.addAttribute("name", name);
		return "hello-template";
	}
	
	/*
	 @ResponseBody
	 	- viewResolver 대신에 HttpMessageConverter가 동작
	 	- 기본 문자처리 : StringHttpMessageConverter
	 	- 기본 객체처리 : MappingJackson2HttpMessageConverter
	 */
	
	@GetMapping("/hello-spring")
	@ResponseBody
	public String helloSpring(@RequestParam("name") String name) {
		return "hello " + name;
	}
	
	@GetMapping("/hello-api")
	@ResponseBody
	public Hello helloApi(@RequestParam("name") String name) {
		Hello hello = new Hello();
		hello.setName(name);
		return hello;
	}
	
	static class Hello {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
