package hello.jdbc.service;

import org.springframework.transaction.annotation.Transactional;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

/**
 * 예외 문제 해결 - 체크 예외를 런타임 예외로 변경(throws SQLException 변경)
 */
@RequiredArgsConstructor
public class MemberServiceV4 {
    private final MemberRepository repository;

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
        Member fromMember = repository.findById(fromId);
        Member toMember = repository.findById(toId);

        repository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        repository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if("ex".equals(toMember.getMemberId())) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
