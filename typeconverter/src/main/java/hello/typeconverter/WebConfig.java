package hello.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	/*
	메시지 컨버터(HttpMessageConverter)에는 컨버전 서비스가 적용되지 않는다.
	
	특히 객체를 JSON으로 변환할 때 메시지 컨버터를 사용하면서 이 부분을 많이 오해하는데, 
	HttpMessageConverter 의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에 입력하는 것이다.
	
	예를 들어서 JSON을 객체로 변환하는 메시지 컨버터는 내부에서 Jackson 같은 라이브러리를 사용한다.
	객체를 JSON으로 변환한다면 그 결과는 이 라이브러리에 달린 것이다.
	따라서 JSON 결과로 만들어지는 숫자나 날짜 포맷을 변경하고 싶으면 해당 라이브러리가 제공하는 설정을 통해서 포맷을 지정해야 한다.
	*/
	@Override
	public void addFormatters(FormatterRegistry registry) {
		//컨버터를 추가하면 추가한 컨버터가 기본 컨버터 보다 높은 우선 순위를 가진다.
		//registry.addConverter(new StringToIntegerConverter());
		//registry.addConverter(new IntegerToStringConverter());
		registry.addConverter(new StringToIpPortConverter());
		registry.addConverter(new IpPortToStringConverter());
		
		//기능(숫자 -> 문자, 문자 -> 숫자)이 같고 converter 가 우선순위가 더 높아서 주석처리
		registry.addFormatter(new MyNumberFormatter());
	}
}
