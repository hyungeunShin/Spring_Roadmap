package hello.http.service;

import java.util.List;

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
	public Long join(Member member) {
		return memberRepository.save(member).getId();
	}

	@Override
	public Member update(Member member) {
		return memberRepository.update(member);
	}

	@Override
	public void delete(Long id) {
		memberRepository.delete(id);
	}
}	
