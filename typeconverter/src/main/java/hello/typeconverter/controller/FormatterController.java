package hello.typeconverter.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Controller
public class FormatterController {
	
	@GetMapping("/formatter/edit")
	public String formatterForm(Model model) {
		Form form = new Form();
		form.setNumber(10000);
		form.setLocalDateTime(LocalDateTime.now());
		
		model.addAttribute("form", form);
		
		return "formatter-form";
	}
	
	@PostMapping("/formatter/edit")
	public String formatterEdit(@ModelAttribute Form form) {
		return "formatter-view";
	}
	
	@Getter
	@Setter
	@ToString
	static class Form {
		/*
		스프링은 자바에서 기본으로 제공하는 타입들에 대해 수 많은 포맷터를 기본으로 제공한다.
		그런데 포맷터는 기본 형식이 지정되어 있기 때문에, 객체의 각 필드마다 다른 형식으로 포맷을 지정하기는 어렵다.
		스프링은 이런 문제를 해결하기 위해 애노테이션 기반으로 원하는 형식을 지정해서 사용할 수 있는 매우 유용한 포맷터 두 가지를 기본으로 제공한다.
		*/
		@NumberFormat(pattern = "###,###")
		private Integer number;
		
		@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private LocalDateTime localDateTime;
	}
}
