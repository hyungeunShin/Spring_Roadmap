package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
*/
@Slf4j
@Component
public class CallServiceV1 {
    private CallServiceV1 service;

    @Autowired
    public void setCallServiceV1(CallServiceV1 service) {
        this.service = service;
    }

    public void external() {
        log.info("call external");
        service.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
