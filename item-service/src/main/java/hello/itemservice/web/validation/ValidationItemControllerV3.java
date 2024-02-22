package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {
    private final ItemRepository itemRepository;
    
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }
    
    /*
    LocalValidatorFactoryBean 을 글로벌 Validator로 등록한다.
    이 Validator는 @NotNull 같은 애노테이션을 보고 검증을 수행한다.
    이렇게 글로벌 Validator가 적용되어 있기 때문에 @Valid, @Validated 만 적용하면 된다.
	검증 오류가 발생하면 FieldError, ObjectError 를 생성해서 BindingResult 에 담아준다.
    */
    //@PostMapping("/add")
    public String addItem1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	//검증시 @Validated @Valid 둘다 사용가능하다.
    	//@Validated 는 스프링 전용 검증 애노테이션이고 @Valid 는 자바 표준 검증 애노테이션이다.
    	
    	//@ModelAttribute -> 각각의 필드 타입 변환시도 -> 변환에 성공한 필드만 BeanValidation 적용
    	//price 에 문자 "A" 입력 -> "A"를 숫자 타입 변환 시도 실패 -> typeMismatch FieldError 추가 -> price 필드는 BeanValidation 적용 X

    	/*
    	NotBlank 라는 오류 코드를 기반으로 MessageCodesResolver 를 통해 다양한 메시지 코드가 순서대로 생성된다.
    	
    	@NotBlank
			NotBlank.item.itemName
			NotBlank.itemName
			NotBlank.java.lang.String
			NotBlank

		@Range
			Range.item.price
			Range.price
			Range.java.lang.Integer
			Range
			
		BeanValidation 메시지 찾는 순서
		1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기
		2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
		3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.
    	*/
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			bindingResult.reject("totalPriceMin", new Object[] {10000, result}, null);
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v3/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }
    
    @PostMapping("/add")
    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			bindingResult.reject("totalPriceMin", new Object[] {10000, result}, null);
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v3/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }
    
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    //@PostMapping("/{itemId}/edit")
    public String edit1(@PathVariable("itemId") Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			bindingResult.reject("totalPriceMin", new Object[] {10000, result}, null);
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v3/editForm";
    	}
    	
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
    
    @PostMapping("/{itemId}/edit")
    public String edit2(@PathVariable("itemId") Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			bindingResult.reject("totalPriceMin", new Object[] {10000, result}, null);
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v3/editForm";
    	}
    	
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }
}