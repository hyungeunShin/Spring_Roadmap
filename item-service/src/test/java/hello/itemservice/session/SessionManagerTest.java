package hello.itemservice.session;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import hello.itemservice.domain.member.Member;
import hello.itemservice.web.session.SessionManager;

public class SessionManagerTest {
	SessionManager sessionManager = new SessionManager();
	
	@Test
	void sessionTest() {
		//HttpServletRequest, HttpservletResponse 객체를 직접 사용할 수 없기 때문에
		//테스트에서 비슷한 역할을 해주는 가짜 MockHttpServletRequest, MockHttpServletResponse 를 사용

		//세션 생성
		MockHttpServletResponse response = new MockHttpServletResponse();
		Member member = new Member();
		sessionManager.createSession(member, response);
		
		//요청에 응답 쿠키 저장
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setCookies(response.getCookies());
		
		//세션 조회
		Object result = sessionManager.getSession(request);
		assertThat(result).isEqualTo(member);
		
		sessionManager.expire(request);
		Object expired = sessionManager.getSession(request);
		assertThat(expired).isNull();
	}
}
