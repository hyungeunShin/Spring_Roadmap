package hello.typeconverter.formatter;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

public class MyNumberFormatterTest {
	MyNumberFormatter formatter = new MyNumberFormatter();
	
	@Test
	void parse() throws ParseException {
		Number result = formatter.parse("1,000", Locale.KOREA);
		assertEquals(1000L, result);
	}
	
	@Test
	void print() {
		String result = formatter.print(1000, Locale.KOREA);
		assertEquals("1,000", result);
	}
}
