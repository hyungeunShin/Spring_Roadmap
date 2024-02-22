package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hello.itemservice.domain.item.Item;

@Component
public class ItemValidator implements Validator {
	//해당 검증기를 지원하는 여부 확인
	@Override
	public boolean supports(Class<?> clazz) {
		//자식 class 까지 커버 가능
		return Item.class.isAssignableFrom(clazz);
	}
	
	//검증 대상 객체와 BindingResult
	@Override
	public void validate(Object target, Errors errors) {
		Item item = (Item) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");
    	
    	if(!StringUtils.hasText(item.getItemName())) {
    		//
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		errors.rejectValue("price", "range", new Object[] {1000, 1000000}, null);
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		errors.rejectValue("quantity", "max", new Object[] {9999}, null);
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			errors.reject("totalPriceMin", new Object[] {10000, result}, null);
    		}
    	}
	}
}
