package hello.exception.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiExceptionController {
	@GetMapping("/api/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if("ex".equals(id)) {
			log.info("예외 발생");
			throw new RuntimeException("잘못된 사용자");
		}
		
		if("bad".equals(id)) {
			//MyHandlerExceptionResolver 를 통해 500에러를 400에러로 처리
			throw new IllegalArgumentException("잘못된 입력 값");
		}
		
		if("user-ex".equals(id)) {
			throw new UserException("사용자 오류");
		}
		
		//API를 요청했는데 정상의 경우 API로 JSON 형식으로 데이터가 정상 반환된다.
		//그런데 오류가 발생하면 우리가 미리 만들어둔 오류 페이지 HTML이 반환된다.(ErrorPageController.class의 errorPage500() 호출)
		//JSON으로 응답하기 위해 errorPage500API() 추가
		return new MemberDto(id, "hello " + id);
	}
	
	/*
	어노테이션을 활용한 예외처리
	1. ExceptionHandlerExceptionResolver
	2. ResponseStatusExceptionResolver -> HTTP 응답 코드 변경
	3. DefaultHandlerExceptionResolver -> 스프링 내부 예외 처리, 우선 순위가 가장 낮다. 
	*/
	//ResponseStatusExceptionResolver
	@GetMapping("/api/response-status-ex1")
	public String responseStatusEx1() {
		/*
		@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류입니다.")
		ResponseStatusExceptionResolver 예외가 해당 애노테이션을 확인해서 오류 코드를 HttpStatus.BAD_REQUEST(400)으로 변경하고 메시지도 담는다.
		
		ResponseStatusExceptionResolver 코드를 확인해보면 결국 response.sendError(statusCode, resolvedReason) 를 호출하는 것을 확인할 수 있다.
		sendError(400) 를 호출했기 때문에 WAS에서 다시 오류 페이지(/error)를 내부 요청한다.
		
		@ResponseStatus 는 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다.
		(애노테이션을 직접 넣어야 하는데 내가 코드를 수정할 수 없는 라이브러리의 예외 코드 같은 곳에는 적용할 수 없다.)
		추가로 애노테이션을 사용하기 때문에 조건에 따라 동적으로 변경하는 것도 어렵다.
		*/
		throw new BadRequestException();
	}
	
	@GetMapping("/api/response-status-ex2")
	public String responseStatusEx2() {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 요청입니다. by ResponseStatusException", new IllegalArgumentException());
	}
	
	//DefaultHandlerExceptionResolver
	@GetMapping("/api/default-handler-ex")
	public String defaultException(@RequestParam("data") Integer data) {
		/*
		DefaultHandlerExceptionResolver 는 스프링 내부에서 발생하는 스프링 예외를 해결한다.
		대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생하는데,
		이 경우 예외가 발생했기 때문에 그냥 두면 서블릿 컨테이너까지 오류가 올라가고, 결과적으로 500 오류가 발생한다.
		그런데 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제이다.
		HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.
		DefaultHandlerExceptionResolver 는 이것을 500 오류가 아니라 HTTP 상태 코드 400 오류로 변경한다. 
		*/
		return "ok";
		
		//localhost/api/default-handler-ex?data=qqq
		/*
		{
		    "timestamp": "2024-02-27T01:11:36.001+00:00",
		    "status": 400,
		    "error": "Bad Request",
		    "exception": "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException",
		    "message": "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"qqq\"",
		    "path": "/api/default-handler-ex"
		} 
		*/
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	static class MemberDto {
		private String memberId;
		private String name;
	}
}
