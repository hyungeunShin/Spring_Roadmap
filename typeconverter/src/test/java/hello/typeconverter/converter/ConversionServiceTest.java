package hello.typeconverter.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

import hello.typeconverter.type.IpPort;

public class ConversionServiceTest {
	//스프링은 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데 이것이 바로 컨버전 서비스(ConversionService)이다.

	@Test
	void conversionService() {
		DefaultConversionService conversionService = new DefaultConversionService();
		//DefaultConversionService 는 다음 두 인터페이스를 구현했다.
		//ConversionService : 컨버터 사용에 초점
		//ConverterRegistry : 컨버터 등록에 초점
		conversionService.addConverter(new StringToIntegerConverter());
		conversionService.addConverter(new IntegerToStringConverter());
		conversionService.addConverter(new StringToIpPortConverter());
		conversionService.addConverter(new IpPortToStringConverter());
		
		assertEquals(10, conversionService.convert("10", Integer.class));
		assertEquals("10", conversionService.convert(10, String.class));
		
		assertEquals(new IpPort("127.0.0.1", 80), conversionService.convert("127.0.0.1:80", IpPort.class));
		assertEquals("127.0.0.1:80", conversionService.convert(new IpPort("127.0.0.1", 80), String.class));
	}
}
