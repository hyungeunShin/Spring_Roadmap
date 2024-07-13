package hello;

public class A {
    /*
    라이브러리 관리의 어려움
        프로젝트를 처음 시작하면 어떤 라이브러리들을 사용할지 고민하고 선택해야 한다.
        예를 들어서 스프링 WEB, 내장 톰캣, JSON 처리기, 로거 등등 수 많은 라이브러리를 선택해야 한다.
        여기에 추가로 각 라이브러리의 버전까지 고민해야 한다.
        더 심각한 문제는 각 라이브러리들끼리 호환이 잘 되는 버전도 있지만 잘 안되는 버전들도 있다.
        과거에는 이런 문제들 때문에 처음 프로젝트를 세팅하는데 상당히 많은 시간을 소비했다.

        스프링 부트는 개발자가 라이브러리들을 편리하게 사용할 수 있는 다양한 기능들을 제공한다.
            - 외부 라이브러리 버전 관리
            - 스프링 부트 스타터 제공

    스프링 부트 라이브러리 버전 관리
        스프링 부트는 개발자 대신에 수 많은 라이브러리의 버전을 직접 관리해준다.
        이제 개발자는 원하는 라이브러리만 고르고 라이브러리의 버전은 생략해도 된다.
        그러면 스프링 부트가 부트 버전에 맞춘 최적화된 라이브러리 버전을 선택해준다.
        버전 관리 기능을 사용하려면 io.spring.dependency-management 플러그인을 사용해야 한다.

        ※ 참고 - 스프링 부트가 관리하는 외부 라이브러리 버전을 확인하는 방법
            https://docs.spring.io/spring-boot/appendix/dependency-versions/coordinates.html#appendix.dependency-versions.coordinates
        ※ 참고 - 스프링 부트가 관리하지 않는 라이브러리
            스프링 부트가 관리하지 않는 외부 라이브러리도 있다.
            특히 아직 잘 알려지지 않거나 대중적이지 않은 경우가 그러한데, 이때는 다음과 같이 라이브러리의 버전을 직접 적어주어야 한다.
            implementation 'org.yaml:snakeyaml:2.2'

    스프링 부트 스타터
        - 앞서 보았듯이 웹 프로젝트를 하나 실행하려면 생각보다 수 많은 라이브러리가 필요하다.
        - 스프링 웹 MVC, 내장 톰캣, JSON 처리, 스프링 부트 관련, LOG, YML 등등 다양한 라이브러리가 사용된다.
        - 개발자 입장에서는 그냥 웹 프로젝트를 하나 시작하고 싶은 것이고, 일반적으로 많이 사용하는 대중적인 라이브러리들을 포함해서 간단하게 시작하고 싶을 것이다.
        - 스프링 부트는 이런 문제를 해결하기 위해 프로젝트를 시작하는데 필요한 관련 라이브러리를 모아둔 스프링부트 스타터를 제공한다.
        - 스프링 부트 스타터 덕분에 누구나 쉽고 편리하게 프로젝트를 시작할 수 있다.

    스프링 부트 스타터 - 자주 사용하는 것 위주
        - spring-boot-starter : 핵심 스타터, 자동 구성, 로깅, YAML
        - spring-boot-starter-jdbc : JDBC, HikariCP 커넥션풀
        - spring-boot-starter-data-jpa : 스프링 데이터 JPA, 하이버네이트
        - spring-boot-starter-data-mongodb : 스프링 데이터 몽고
        - spring-boot-starter-data-redis : 스프링 데이터 Redis, Lettuce 클라이언트
        - spring-boot-starter-thymeleaf : 타임리프 뷰와 웹 MVC
        - spring-boot-starter-web : 웹 구축을 위한 스타터, RESTful, 스프링 MVC, 내장 톰캣
        - spring-boot-starter-validation : 자바 빈 검증기(하이버네이트 Validator)
        - spring-boot-starter-batch : 스프링 배치를 위한 스타터

        스프링 부트 스타터의 전체 목록은 다음 공식 메뉴얼을 참고
            https://docs.spring.io/spring-boot/reference/using/build-systems.html#using.build-systems.starters

    라이브러리 버전 변경
        외부 라이브러리의 버전을 변경하고 싶을 때 다음과 같은 형식으로 편리하게 변경할 수 있다.
            ext['tomcat.version'] = '10.1.24'

        ※ 스프링 부트가 관리하는 외부 라이브러리 버전 변경에 필요한 속성 값
            https://docs.spring.io/spring-boot/appendix/dependency-versions/properties.html#appendix.dependency-versions.properties
    */
}
