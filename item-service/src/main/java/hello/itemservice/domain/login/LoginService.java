package hello.itemservice.domain.login;

import org.springframework.stereotype.Service;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final MemberRepository memberRepository;
	
	public Member login(String loginId, String password) {
		/*
		Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
		Member member = findMemberOptional.get();
		if(member.getPassword().equals(password)) {
			return member;
		} else {
			return null;
		}
		*/
		
		return memberRepository.findByLoginId(loginId)
			.filter(member -> member.getPassword().equals(password))
			.orElse(null);
	}
}
