package hello.http.repository;

import java.util.List;
import java.util.Optional;

import hello.http.domain.Member;

public interface MemberRepository {
	public List<Member> findAll();
	
	public Optional<Member> findById(Long id);
	
	public Member save(Member member);
	
	public Member update(Member member);
	
	public Member delete(Long id);
}
