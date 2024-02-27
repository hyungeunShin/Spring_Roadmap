package hello.exception.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiExceptionV2Controller {
	/*
	HandlerExceptionResolver 를 직접 사용하기는 복잡하다.
	API 오류 응답의 경우 response 에 직접 데이터를 넣어야 해서 매우 불편하고 번거롭다.
	ModelAndView 를 반환해야 하는 것도 API에는 잘 맞지 않는다.
	스프링은 이 문제를 해결하기 위해 @ExceptionHandler 라는 예외 처리 기능을 제공한다.
	
	스프링은 API 예외 처리 문제를 해결하기 위해 @ExceptionHandler 라는 애노테이션을 사용하는 매우 편리한 예외 처리 기능을 제공한다.
	이것이 바로 ExceptionHandlerExceptionResolver 이다.
	스프링은 ExceptionHandlerExceptionResolver 를 기본으로 제공하고, 기본으로 제공하는 ExceptionResolver 중에 우선순위도 가장 높다.
	실무에서 API 예외 처리는 대부분 이 기능을 사용한다.
	
	https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html#mvc-ann-exceptionhandler-args
	*/
	
	/*
	//상태코드도 변경 가능하다
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalExHandler(IllegalArgumentException e) {
		log.error("[illegalExHandler] exception", e);
		return new ErrorResult("BAD", e.getMessage());
	}
	
	
	@ExceptionHandler //(UserException.class) 생략하면 메서드 파라미터의 예외가 지정
	public ResponseEntity<ErrorResult> userExHandler(UserException e) {
		log.error("[userException] exception", e);
		return new ResponseEntity<>(new ErrorResult("USER-EX", e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	*/
	
	/*
	@ExceptionHandler 에 지정한 부모 클래스는 자식 클래스까지 처리할 수 있다.
	따라서 자식예외 가 발생하면 부모 예외처리(), 자식예외처리() 둘 다 호출 대상이 된다.
	그런데 둘 중 더 자세한 것이 우선권을 가지므로 자식예외처리() 가 호출된다.
	물론 부모예외 가 호출되면 부모예외처리() 만 호출 대상이 되므로 부모예외처리() 가 호출된다 
	*/
	/*
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	public ErrorResult exHandler(Exception e) {
		log.error("[Exception] exception", e);
		return new ErrorResult("EX", "내부오류");
	}
	*/
	
	@GetMapping("/api2/members/{id}")
	public MemberDto getMember(@PathVariable("id") String id) {
		if("ex".equals(id)) {
			log.info("예외 발생");
			throw new RuntimeException("잘못된 사용자");
		}
		
		if("bad".equals(id)) {
			throw new IllegalArgumentException("잘못된 입력 값");
		}
		
		if("user-ex".equals(id)) {
			throw new UserException("사용자 오류");
		}
		
		return new MemberDto(id, "hello " + id);
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
