package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentCheck {
    /*
    커맨드 라인 옵션 인수 실행(Program arguments)
        --url=devdb --username=dev_user --password=dev_pw
    자바 시스템 속성 실행(Add VM options)
        -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw

    커맨드 라인 옵션 인수, 자바 시스템 속성 모두 Environment 를 통해서 동일한 방법으로 읽을 수 있다.
    스프링은 Environment 를 통해서 외부 설정을 읽는 방법을 추상화했다.
    덕분에 자바 시스템 속성을 사용하다가 만약 커맨드 라인 옵션 인수를 사용하도록 읽는 방법이 변경되어도 개발 소스 코드는 전혀 변경하지 않아도 된다.

    우선순위
        예를 들어서 커맨드 라인 옵션 인수와 자바 시스템 속성을 다음과 같이 중복해서 설정하면 어떻게 될까?
            - 커맨드 라인 옵션 인수 실행
                --url=proddb --username=prod_user --password=prod_pw
            - 자바 시스템 속성 실행
                -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw

        우선순위는 상식 선에서 딱 2가지만 기억하면 된다.
            - 더 유연한 것이 우선권을 가진다. (변경하기 어려운 파일 보다 실행시 원하는 값을 줄 수 있는 자바 시스템 속성이 더 우선권을 가진다.)
            - 범위가 넒은 것 보다 좁은 것이 우선권을 가진다. (자바 시스템 속성은 해당 JVM 안에서 모두 접근할 수 있다. 반면에 커맨드 라인 옵션 인수는 main 의 arg 를 통해서 들어오기 때문에 접근 범위가 더 좁다.)

        자바 시스템 속성과 커맨드 라인 옵션 인수의 경우 커맨드 라인 옵션 인수의 범위가 더 좁기 때문에 커맨드 라인 옵션 인수가 우선권을 가진다.
    */

    private final Environment env;

    public EnvironmentCheck(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        log.info("env url : {}", env.getProperty("url"));
        log.info("env username : {}", env.getProperty("username"));
        log.info("env password : {}", env.getProperty("password"));
    }
}
