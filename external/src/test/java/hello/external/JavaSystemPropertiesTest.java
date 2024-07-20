package hello.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
class JavaSystemPropertiesTest {
    /*
    자바 시스템 속성(Java System properties)은 실행한 JVM 안에서 접근 가능한 외부 설정이다.
    추가로 자바가 내부에서 미리 설정해두고 사용하는 속성들도 있다.
        - System.getProperties() 를 사용하면 Map 과 유사한( Map 의 자식 타입) key=value 형식의 Properties 를 받을 수 있다.
          이것을 통해서 모든 자바 시스템 속성을 조회할 수 있다.
        - System.getProperty(key) 를 사용하면 속성값을 조회할 수 있다.
        - 자바가 기본으로 제공하는 수 많은 속성들이 추가되어 있는 것을 확인할 수 있다.
          자바는 내부에서 필요할때 이런 속성들을 사용하는데, 예를 들어서 file.encoding=UTF-8 를 통해서 기본적인 파일 인코딩 정보 등으로 사용한다.

    Jar 실행
        jar 로 빌드되어 있다면 실행시 다음과 같이 자바 시스템 속성을 추가할 수 있다.
            java -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw -jar external-0.0.1-SNAPSHOT.jar

    자바 시스템 속성을 자바 코드로 설정
        자바 시스템 속성은 앞서 본 것 처럼 -D 옵션을 통해 실행 시점에 전달하는 것도 가능하고, 다음과 같이 자바 코드 내부에서 추가하는 것도 가능하다.
        코드에서 추가하면 이후에 조회시에 값을 조회할 수 있다.
            - 설정: System.setProperty(propertyName, "propertyValue")
            - 조회: System.getProperty(propertyName)
    */
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        properties.forEach((key, value) -> log.info("{} : {}", key, value));

        /*
        1. Edit Configurations
        2. 현재 class 에 Modify options
        3. Add VM options
            -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw
        */
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url : {}", url);
        log.info("username : {}", username);
        log.info("password : {}", password);
    }
}
