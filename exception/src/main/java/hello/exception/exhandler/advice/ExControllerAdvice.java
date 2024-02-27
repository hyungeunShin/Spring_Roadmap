package hello.exception.exhandler.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import hello.exception.api.ApiExceptionV2Controller;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(assignableTypes = ApiExceptionV2Controller.class)
public class ExControllerAdvice {
	//@ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
	//@ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
	/*
	대상을 적용하고 싶으면
	@ControllerAdvice(annotations = RestController.class)
	@ControllerAdvice("org.example.controllers")
	@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
	*/
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResult illegalExHandler(IllegalArgumentException e) {
		log.error("[illegalExHandler] exception", e);
		return new ErrorResult("BAD", e.getMessage());
	}
	
	
	@ExceptionHandler
	public ResponseEntity<ErrorResult> userExHandler(UserException e) {
		log.error("[userException] exception", e);
		return new ResponseEntity<>(new ErrorResult("USER-EX", e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler
	public ErrorResult exHandler(Exception e) {
		log.error("[Exception] exception", e);
		return new ErrorResult("EX", "내부오류");
	}
}
