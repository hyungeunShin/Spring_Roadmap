package hello.http.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hello.http.domain.Member;
import hello.http.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;

	@Override
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	@Override
	public Member findOne(Long memberId) throws Exception {
		return memberRepository.findById(memberId).orElseThrow(() -> new NullPointerException("없는 회원"));
	}

	@Override
	public Member join(Member member) throws Exception {
		Optional<Member> result = memberRepository.findById(member.getId());
		
		if(result.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 아이디");
		} else {
			return memberRepository.save(member);
		}
	}

	@Override
	public Map<String, Object> overwrite(Member member) {
		Map<String, Object> map = new HashMap<>();
		Optional<Member> result = memberRepository.findById(member.getId());
		
		if(result.isEmpty()) {
			map.put("key", 1);
			memberRepository.save(member);
		} else {
			map.put("key", 2);
			memberRepository.save(member);
		}
		
		map.put("member", member);
		
		return map;
	}
	
	@Override
	public Member update(Member member) throws Exception {
		Optional<Member> result = memberRepository.findById(member.getId());
		
		if(result.isEmpty()) {
			throw new NullPointerException("없는 회원");
		} else {
			return memberRepository.update(member);
		}
	}
	
	@Override
	public Member delete(Long id) throws Exception {
		Optional<Member> result = memberRepository.findById(id);
		
		if(result.isEmpty()) {
			throw new NullPointerException("없는 회원");
		} else {
			return memberRepository.delete(id);
		}
	}
}	
