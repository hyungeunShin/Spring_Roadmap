package hello.servlet.web.frontcontroller.v1.controller;

import java.io.IOException;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberSaveControllerV1 implements ControllerV1 {
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		int age = Integer.parseInt(request.getParameter("age"));
		
		Member member = new Member(username, age);
		System.out.println("member = " + member);
		
		MemberRepository memberRepository = MemberRepository.getInstance();
		memberRepository.save(member);
		
		//Model에 데이터를 보관한다.
		request.setAttribute("member", member);
		
		request.getRequestDispatcher("/WEB-INF/views/save-result.jsp").forward(request, response);
	}
}
