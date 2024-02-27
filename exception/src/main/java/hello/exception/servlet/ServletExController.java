package hello.exception.servlet;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ServletExController {
	//WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
	@GetMapping("/error-ex")
	public void errorEx() {
		throw new RuntimeException("예외 발생!");
	}
	
	//WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError())
	///error-page/404 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View
	@GetMapping("/error-404")
	public void error404(HttpServletResponse response) throws IOException {
		/*
		response.sendError() 를 호출하면 response 내부에는 오류가 발생했다는 상태를 저장해둔다.
		그리고 서블릿 컨테이너는 고객에게 응답 전에 response 에 sendError() 가 호출되었는지 확인한다.
		그리고 호출되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보여준다.
		*/
		response.sendError(404, "404 오류!");
	}

	@GetMapping("/error-400")
	public void error400(HttpServletResponse response) throws IOException {
		response.sendError(400);
	}
	
	@GetMapping("/error-500")
	public void error500(HttpServletResponse response) throws IOException {
		response.sendError(500);
	}
}
