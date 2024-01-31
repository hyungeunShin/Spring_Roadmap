package hello.http.service;

import java.util.List;
import java.util.Map;

import hello.http.domain.Member;

public interface MemberService {
	public List<Member> findMembers();
	
	public Member findOne(Long memberId) throws Exception;
	
	public Member join(Member member) throws Exception;
	
	public Map<String, Object> overwrite(Member member);
	
	public Member update(Member member) throws Exception;
	
	public Member delete(Long id) throws Exception;
}
