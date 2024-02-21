package hello.itemservice.validation;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {
	/*
	MessageCodesResolver
		- 검증 오류 코드로 메시지 코드들을 생성한다.
		- MessageCodesResolver 인터페이스이고 DefaultMessageCodesResolver 는 기본 구현체이다.
		- 주로 다음과 함께 사용 ObjectError, FieldError
	*/
	
	MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();
	
	@Test
	void messageCodesResolverObject() {
		String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
		
		for(String messageCode : messageCodes) {
			//디테일한게 먼저
			//messageCode : required.item
			//messageCode : required
			System.out.println("messageCode : " + messageCode);
		}
		
		assertThat(messageCodes).containsExactly("required.item", "required");
	}
	
	@Test
	void messageCodesResolverField() {
		String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
		
		for(String messageCode : messageCodes) {
			//messageCode : required.item.itemName
			//messageCode : required.itemName
			//messageCode : required.java.lang.String
			//messageCode : required
			System.out.println("messageCode : " + messageCode);
		}
		
		assertThat(messageCodes).containsExactly(
				"required.item.itemName",
				"required.itemName",
				"required.java.lang.String",
				"required"
		);
	}
}
