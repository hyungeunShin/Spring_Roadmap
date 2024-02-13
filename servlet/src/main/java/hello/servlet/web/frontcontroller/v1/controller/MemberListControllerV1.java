package hello.servlet.web.frontcontroller.v1.controller;

import java.io.IOException;
import java.util.List;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberListControllerV1 implements ControllerV1 {
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberRepository memberRepository = MemberRepository.getInstance();

		List<Member> members = memberRepository.findAll();
		request.setAttribute("members", members);

		request.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(request, response);
	}
}
