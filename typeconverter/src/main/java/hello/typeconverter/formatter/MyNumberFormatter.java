package hello.typeconverter.formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
	//객체를 특정한 포멧에 맞추어 문자로 출력하거나 또는 그 반대의 역할을 하는 것에 특화된 기능이 바로 포맷터	(Formatter)
	
	//parse() 를 사용해서 문자를 숫자로 변환한다.
	//참고로 Number 타입은 Integer , Long 과 같은 숫자 타입의 부모 클래스이다.
	@Override
	public Number parse(String text, Locale locale) throws ParseException {
		log.info("text : {}, locale : {}", text, locale);
		//"1,000" -> 1000
		NumberFormat format = NumberFormat.getInstance(locale);
		return format.parse(text);
	}
	
	//print() 를 사용해서 객체를 문자로 변환한다.
	@Override
	public String print(Number object, Locale locale) {
		log.info("object : {}, locale : {}", object, locale);
		return NumberFormat.getInstance(locale).format(object);
	}
}
