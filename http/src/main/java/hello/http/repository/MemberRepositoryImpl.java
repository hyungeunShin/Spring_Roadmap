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
		store.put(member.getId(), member);
		return member;
	}
	
	@Override
	public Member update(Member member) {
		Member target = store.get(member.getId());
		member.setName(member.getName() == null ? target.getName() : member.getName());
		member.setAge(member.getAge() == null ? target.getAge() : member.getAge());
		store.put(member.getId(), member);
		return member;
	}

	@Override
	public Member delete(Long id) {
		Member member = store.get(id);
		store.remove(id);
		return member;
	}
}
