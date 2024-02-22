package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication /* implements WebMvcConfigurer */ {
	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
	
	/*
	//스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		
		//설정 파일의 이름을 지정
		//messages 로 지정하면 messages.properties 파일을 읽어서 사용한다.
		//추가로 국제화 기능을 적용하려면 messages_en.properties, messages_ko.properties와 같이 파일명 마지막에 언어 정보를 주면된다.
		messageSource.setBasenames("messages", "errors");
		
		//인코딩 정보를 지정
		messageSource.setDefaultEncoding("utf-8");
		
		return messageSource;
	}
	*/
	
	/*
	public Validator getValidator() {
		return new ItemValidator();
	}
	*/
}
