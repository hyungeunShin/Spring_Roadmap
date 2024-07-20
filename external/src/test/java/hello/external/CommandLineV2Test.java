package hello.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;
import java.util.Set;

@Slf4j
class CommandLineV2Test {
    /*
    일반적인 커맨드 라인 인수
        커맨드 라인에 전달하는 값은 형식이 없고, 단순히 띄어쓰기로 구분한다.
            - aaa bbb [aaa, bbb] 값 2개
            - hello world [hello, world] 값 2개
            - "hello world" [hello world] (공백을 연결하려면 " 를 사용하면 된다.) 값 1개
            - key=value [key=value] 값 1개

    커맨드 라인 옵션 인수(command line option arguments)
        커맨드 라인 인수를 key=value 형식으로 구분하는 방법이 필요하다.
        그래서 스프링에서는 커맨드 라인 인수를 key=value 형식으로 편리하게 사용할 수 있도록 스프링 만의 표준 방식을 정의했는데, 그것이 바로 커맨드 라인 옵션 인수이다.
        스프링은 커맨드 라인에 -(dash) 2개 (--)를 연결해서 시작하면 key=value 형식으로 정하고 이것을 커맨드 라인 옵션 인수라 한다.
            --key=value 형식으로 사용한다.
            --username=userA --username=userB 하나의 키에 여러 값도 지정할 수 있다.

    스프링이 제공하는 ApplicationArguments 인터페이스와 DefaultApplicationArguments 구현체를 사용하면 커맨드 라인 옵션 인수를 규격대로 파싱해서 편리하게 사용할 수 있다.
    */
    public static void main(String[] args) {
        for(String arg : args) {
            log.info("{}", arg);
        }

        ApplicationArguments appArgs = new DefaultApplicationArguments(args);
        log.info("sourceArgs : {}", List.of(appArgs.getSourceArgs()));
        log.info("nonOptionArgs : {}", appArgs.getNonOptionArgs());
        log.info("optionNames : {}", appArgs.getOptionNames());

        Set<String> optionNames = appArgs.getOptionNames();
        for(String optionName : optionNames) {
            log.info("{} : {}", optionName, appArgs.getOptionValues(optionName));
        }

        //옵션 인수는 --username=userA --username=userB 처럼 하나의 키에 여러 값을 포함할 수 있기 때문에 appArgs.getOptionValues(key) 의 결과는 리스트(List)를 반환
        List<String> url = appArgs.getOptionValues("url");
        log.info("url : {}", url);
        log.info("username : {}", appArgs.getOptionValues("username"));
        log.info("password : {}", appArgs.getOptionValues("password"));
        log.info("mode : {}", appArgs.getOptionValues("mode"));
    }
}
