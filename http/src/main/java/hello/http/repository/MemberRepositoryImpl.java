package hello.http.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import hello.http.domain.Member;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
	private static Map<Long, Member> store = new HashMap<>();
	
	private static long sequence = 0L;
	
	@Override
	public List<Member> findAll() {
		return new ArrayList<>(store.values());
	}
	
	@Override
	public Optional<Member> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}
	
	@Override
	public Member save(Member member) {
		member.setId(++sequence);
		store.put(member.getId(), member);
		return member;
	}

	@Override
	public Member update(Member member) {
		Member updateMember = store.get(member.getId());
		updateMember.setName(member.getName());
		store.put(member.getId(), updateMember);
		return updateMember;
	}

	@Override
	public void delete(Long id) {
		store.remove(id);
	}
}
