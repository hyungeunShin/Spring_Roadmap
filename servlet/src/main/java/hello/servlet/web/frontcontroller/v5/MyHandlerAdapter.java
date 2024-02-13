package hello.servlet.web.frontcontroller.v5;

import java.io.IOException;

import hello.servlet.web.frontcontroller.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MyHandlerAdapter {
	/**
	 * @param handler 	: 컨트롤러
	 * @return			: 어댑터가 해당 컨트롤러를 처리할 수 있는지 여부
	 */
	public boolean supports(Object handler);
	
	/**
	 * 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView를 반환해야 한다.
	 * 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야 한다.
	 * 이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.  
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
