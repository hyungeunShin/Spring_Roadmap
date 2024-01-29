package hello.core.web;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogDemoController {
	private final LogDemoService logDemoService;
	
	//에러 발생
	//실제 고객의 요청이 들어와야 생성할 수 있음 => 실제 고객 요청이 왔을때로 지연시켜야 함
	//@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) 일 때
	private final MyLogger myLogger;
	
	//@Scope(value = "request") 일 때
	//private final ObjectProvider<MyLogger> myLoggerProvider;
	
	@GetMapping("log-demo")
	@ResponseBody
	public ResponseEntity<String> logDemo(HttpServletRequest request) {
		//MyLogger myLogger = myLoggerProvider.getObject();
		
		System.out.println("myLogger : " + myLogger.getClass());
		//myLogger : class hello.core.common.MyLogger$$SpringCGLIB$$0
		//CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
		//가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
		//가짜 프록시 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 사실 원본인지 아닌지도 모르게, 동일하게 사용할 수 있다(다형성)
		
		String requestURL = request.getRequestURL().toString();
		myLogger.setRequestURL(requestURL);
		myLogger.log("controller test");
		logDemoService.logic("testId");
		return new ResponseEntity<String>(HttpStatus.OK.toString(), HttpStatus.OK);
	}
}
