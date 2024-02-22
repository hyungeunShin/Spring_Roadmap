package hello.itemservice.validation;

import java.util.Set;

import org.junit.jupiter.api.Test;

import hello.itemservice.domain.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class BeanValidationTest {
	@Test
	void beanValidation() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Item item = new Item();
		item.setItemName("   ");
		item.setPrice(0);
		item.setQuantity(10000);
		
		Set<ConstraintViolation<Item>> violations = validator.validate(item);
		for(ConstraintViolation<Item> violation : violations) {
			//violation=ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class hello.itemservice.domain.item2.Item2, messageTemplate='{jakarta.validation.constraints.NotBlank.message}'}
			//violation.message=공백일 수 없습니다
			//violation=ConstraintViolationImpl{interpolatedMessage='9999 이하여야 합니다', propertyPath=quantity, rootBeanClass=class hello.itemservice.domain.item2.Item2, messageTemplate='{jakarta.validation.constraints.Max.message}'}
			//violation.message=9999 이하여야 합니다
			//violation=ConstraintViolationImpl{interpolatedMessage='1000에서 1000000 사이여야 합니다', propertyPath=price, rootBeanClass=class hello.itemservice.domain.item2.Item2, messageTemplate='{org.hibernate.validator.constraints.Range.message}'}
			//violation.message=1000에서 1000000 사이여야 합니다
					
			System.out.println("violation=" + violation);
			System.out.println("violation.message=" + violation.getMessage());
		}
	}
}
