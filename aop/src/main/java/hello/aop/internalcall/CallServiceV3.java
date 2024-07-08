package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
구조를 분리(권장)
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {
    private final InternalService service;

    public void external() {
        log.info("call external");
        service.internal();
    }
}
