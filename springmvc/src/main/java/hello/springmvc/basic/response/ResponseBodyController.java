package hello.springmvc.basic.response;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;

//@Controller
@RestController
public class ResponseBodyController {
	@GetMapping("/response-body-string-v1")
	public void responseBodyV1(HttpServletResponse response) throws IOException {
		response.getWriter().write("ok");
	}
	
	//ResponseEntity는 HTTP Response 전체를 표현하기 때문에, 이 타입을 반환하는 메소드는 자동으로 @ResponseBody가 적용된 것과 같다.
	@GetMapping("/response-body-string-v2")
	public ResponseEntity<String> responseBodyV2() {
		return new ResponseEntity<>("ok", HttpStatus.OK);
	}

	//@ResponseBody
	@GetMapping("/response-body-string-v3")
	public String responseBodyV3() {
		return "ok";
	}

	@GetMapping("/response-body-json-v1")
	public ResponseEntity<HelloData> responseBodyJsonV1() {
		HelloData helloData = new HelloData();
		helloData.setUsername("userA");
		helloData.setAge(20);
		return new ResponseEntity<>(helloData, HttpStatus.OK);
	}

	@ResponseStatus(HttpStatus.OK)
	//@ResponseBody
	@GetMapping("/response-body-json-v2")
	public HelloData responseBodyJsonV2() {
		HelloData helloData = new HelloData();
		helloData.setUsername("userA");
		helloData.setAge(20);
		return helloData;
	}
}
