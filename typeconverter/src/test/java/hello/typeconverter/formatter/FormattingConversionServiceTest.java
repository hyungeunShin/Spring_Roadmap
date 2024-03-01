package hello.typeconverter.formatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;

public class FormattingConversionServiceTest {
	/*
	컨버전 서비스에는 컨버터만 등록할 수 있고 포맷터를 등록할 수 없다.
	그런데 생각해보면 포맷터는 객체 -> 문자, 문자 -> 객체로 변환하는 특별한 컨버터일 뿐이다.
	
	포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다.
	내부에서 어댑터 패턴을 사용해서 Formatter 가 Converter 처럼 동작하도록 지원한다.
	FormattingConversionService 는 포맷터를 지원하는 컨버전 서비스이다.
	DefaultFormattingConversionService 는 FormattingConversionService 에 기본적인 통화, 숫자 관련 몇 가지 기본 포맷터를 추가해서 제공한다. 
	*/
	@Test
	void formattingConversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		//FormattingConversionService 는 ConversionService 관련 기능을 상속받기 때문에 컨버터, 포맷터 모두 등록할 수 있다.
		//그리고 사용할 때는 ConversionService 가 제공하는 convert 를 사용하면 된다.
		
		//컨버터 등록
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());
		//포맷터 등록
		conversionService.addFormatter(new MyNumberFormatter());
		
		assertEquals(new IpPort("127.0.0.1", 80), conversionService.convert("127.0.0.1:80", IpPort.class));
		
		assertEquals("1,000", conversionService.convert(1000, String.class));
		assertEquals(1000L, conversionService.convert("1,000", Long.class));
	}
}
