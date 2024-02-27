package hello.exception.resolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import hello.exception.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			if(ex instanceof UserException) {
				log.info("UserException to 400 error");
				String acceptHeader = request.getHeader("accept");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				
				if("application/json".equals(acceptHeader)) {
					Map<String, Object> result = new HashMap<>();
					result.put("ex", ex.getClass());
					result.put("message", ex.getMessage());
					
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(objectMapper.writeValueAsString(result));
					return new ModelAndView();
				} else {
					return new ModelAndView("error/500");
				}
			}
		} catch(IOException e) {
			log.error("reolsver ex", e);
		}
		
		return null;
	}
	
	/*
	ExceptionResolver 를 사용하면 컨트롤러에서 예외가 발생해도 ExceptionResolver 에서 예외를 처리해버린다.
	따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고 스프링 MVC에서 예외 처리는 끝이 난다.
	결과적으로 WAS 입장에서는 정상 처리가 된 것이다.
	이렇게 예외를 이곳에서 모두 처리할 수 있다는 것이 핵심이다.
	
	서블릿 컨테이너까지 예외가 올라가면 복잡하고 지저분하게 추가 프로세스가 실행된다.
	반면에 ExceptionResolver 를 사용하면 예외처리가 상당히 깔끔해진다.
	*/
}
