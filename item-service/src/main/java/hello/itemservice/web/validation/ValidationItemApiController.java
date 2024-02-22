package hello.itemservice.web.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {
	@PostMapping("/add")
	public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
		//HttpMessageConverter 는 @ModelAttribute 와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용된다.
		//@RequestBody 는 HttpMessageConverter 단계에서 JSON 데이터를 객체로 변경하지 못하면 이후 단계 자체가 진행되지 않고 예외가 발생한다. 
		//컨트롤러도 호출되지 않고 Validator도 적용할 수 없다
		
		log.info("API 컨트롤러 호출");
		
		if(bindingResult.hasErrors()) {
			log.info("검증 오류 발생 errors = {}", bindingResult);
			return bindingResult.getAllErrors();
		}
		
		log.info("성공 로직 실행");
		return form;
	}
}