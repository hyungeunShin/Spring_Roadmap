package hello.typeconverter.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hello.typeconverter.type.IpPort;

public class ConverterTest {
	/*
	Converter					-> 기본 타입 컨버터
	ConverterFactory			-> 전체 클래스 계층 구조가 필요할 때
	GenericConverter			-> 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
	ConditionalGenericConverter	-> 특정 조건이 참인 경우에만 실행
	https://docs.spring.io/spring-framework/reference/core/validation/convert.html
	*/
	@Test
	void stringToInteger() {
		StringToIntegerConverter converter = new StringToIntegerConverter();
		Integer result = converter.convert("10");
		assertEquals(10, result);
	}
	
	@Test
	void integerToString() {
		IntegerToStringConverter converter = new IntegerToStringConverter();
		String result = converter.convert(10);
		assertEquals("10", result);
	}
	
	@Test
	void stringToIpPort() {
		StringToIpPortConverter converter = new StringToIpPortConverter();
		String source = "127.0.0.1:80";
		IpPort ipPort = converter.convert(source);
		assertEquals(new IpPort("127.0.0.1", 80), ipPort);
	}
	
	@Test
	void ipPortToString() {
		IpPortToStringConverter converter = new IpPortToStringConverter();
		IpPort ipPort = new IpPort("127.0.0.1", 80);
		String result = converter.convert(ipPort);
		assertEquals("127.0.0.1:80", result);
	}
}
