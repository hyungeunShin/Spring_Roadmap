package hello.external;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CommandLineV1Test {
    /*
    커맨드 라인 인수(Command line arguments)는 애플리케이션 실행 시점에 외부 설정값을 main(args) 메서드의 args 파라미터로 전달하는 방법이다.
        1. Edit Configurations
        2. Program arguments 에 dataA dataB 입력
            커맨드 라인 인수는 공백(space)으로 구분

    Jar 실행
        jar 로 빌드되어 있다면 실행시 다음과 같이 커맨드 라인 인수를 추가할 수 있다.
            java -jar project.jar dataA dataB

    key=value 형식 입력
        애플리케이션을 개발할 때는 보통 key=value 형식으로 데이터를 받는 것이 편리하다.
            url=devdb username=dev_user password=dev_pw
        실행 결과를 보면 알겠지만 커맨드 라인 인수는 key=value 형식이 아니다.
        단순히 문자를 여러게 입력 받는 형식인 것이다. 그래서 3가지 문자가 입력되었다.
            url=devdb
            username=dev_user
            password=dev_pw
        이것은 파싱되지 않은, 통 문자이다.
        이 경우 개발자가 = 을 기준으로 직접 데이터를 파싱해서 key=value 형식에 맞도록 분리해야 한다.
        그리고 형식이 배열이기 때문에 루프를 돌면서 원하는 데이터를 찾아야 하는 번거로움도 발생한다.
        실제 애플리케이션을 개발할 때는 주로 key=value 형식을 자주 사용하기 때문에 결국 파싱해서 Map 같은 형식으로 변환하도록 직접 개발해야 하는 번거로움이 있다.
    */
    public static void main(String[] args) {
        for(String arg : args) {
            log.info("{}", arg);
        }
    }
}
