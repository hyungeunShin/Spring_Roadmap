package hello.http.service;

import java.util.List;

import hello.http.domain.Member;

public interface MemberService {
	public List<Member> findMembers();
	
	public Member findOne(Long memberId) throws Exception;
	
	public Long join(Member member);
	
	public Member update(Member member);
	
	public void delete(Long id);
}
