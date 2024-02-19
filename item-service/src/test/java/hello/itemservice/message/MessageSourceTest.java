package hello.itemservice.message;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {
	@Autowired
	private MessageSource messageSource;
	
	@Test
	void helloMessage() {
		//기본값 : spring.messages.fallback-to-system-locale=true
		//스프링에서 Locale에 해당하는 파일을 못 찾을시, 시스템 Locale에 해당하는 파일을 찾는다. 시스템 Locale로 fallback 됨
		String message = messageSource.getMessage("hello", null, null);
		assertEquals("안녕", message);
	}
	
	@Test
	void notFoundMessageCode() {
		//messageSource.getMessage("no_code", null, null);
		assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
			.isInstanceOf(NoSuchMessageException.class);
	}
	
	@Test
	void notFoundMessageCodeDefaultMessage() {
		String message = messageSource.getMessage("no_code", null, "기본 메시지", null);
		assertEquals("기본 메시지", message);
	}
	
	@Test
	void argumentMessage() {
		String message = messageSource.getMessage("hello.name", new Object[] {"Spring"}, null);
		assertEquals("안녕 Spring", message);
	}
	
	@Test
	void defaultLang() {
		assertEquals("안녕", messageSource.getMessage("hello", null, null));
		assertEquals("안녕", messageSource.getMessage("hello", null, Locale.KOREA));
	}
	
	@Test
	void enLang() {
		assertEquals("hello", messageSource.getMessage("hello", null, Locale.ENGLISH));
	}
}
