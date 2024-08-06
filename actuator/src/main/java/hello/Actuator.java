package hello;

public class Actuator {
    /*
    프로덕션 준비 기능이란?
        개발자가 애플리케이션을 개발할 때 기능 요구사항만 개발하는 것은 아니다.
        서비스를 실제 운영 단계에 올리게 되면 개발자들이 해야 하는 또 다른 중요한 업무가 있다.
        바로 서비스에 문제가 없는지 모니터링하고 지표들을 심어서 감시하는 활동들이다.

        운영 환경에서 서비스할 때 필요한 이런 기능들을 프로덕션 준비 기능이라 한다.
        쉽게 이야기해서 프로덕션을 운영에 배포할 때 준비해야 하는 비 기능적 요소들을 뜻한다.
            - 지표(metric), 추적(trace), 감사(auditing)
            - 모니터링

        좀 더 구제적으로 설명하자면 애플리케이션이 현재 살아있는지, 로그 정보는 정상 설정 되었는지, 커넥션 풀은 얼마나 사용되고 있는지 등을 확인할 수 있어야 한다.
        스프링 부트가 제공하는 액추에이터는 이런 프로덕션 준비 기능을 매우 편리하게 사용할 수 있는 다양한 편의 기능들을 제공한다.
        더 나아가서 마이크로미터, 프로메테우스, 그라파나 같은 모니터링 시스템과 매우 쉽게 연동할 수 있는 기능도 제공한다.

        참고로 액추에이터는 시스템을 움직이거나 제어하는 데 쓰이는 기계 장치라는 뜻이다.

    액츄에이터
        엔드포인트 설정
            액츄에이터가 제공하는 기능 하나하나를 엔드포인트라 한다.
            각각의 엔드포인트는 /actuator/{엔드포인트명} 과 같은 형식으로 접근할 수 있다.
            엔드포인트를 사용하려면 다음 2가지 과정이 모두 필요하다.
                1. 엔드포인트 활성화
                2. 엔드포인트 노출
                    application.properties -> management.endpoints.web.exposure.include=*

                엔드포인트를 활성화 한다는 것은 해당 기능 자체를 사용할지 말지 on, off 를 선택하는 것이다.
                엔드포인트를 노출하는 것은 활성화된 엔드포인트를 HTTP 에 노출할지 아니면 JMX 에 노출할지 선택하는 것이다.
                엔드포인트를 활성화하고 추가로 HTTP 를 통해서 웹에 노출할지, 아니면 JMX 를 통해서 노출할지 두 위치에 모두 노출할지 노출 위치를 지정해주어야 한다.
                물론 활성화가 되어있지 않으면 노출도 되지 않는다.
                그런데 엔드포인트는 대부분 기본으로 활성화 되어 있다.(shutdown 제외)
                노출이 되어 있지 않을 뿐이다.
                따라서 어떤 엔드포인트를 노출할지 선택하면 된다.
                참고로 HTTP 와 JMX 를 선택할 수 있는데, 보통 JMX 는 잘 사용하지 않으므로 HTTP 에 어떤 엔드포인트를 노출할지 선택하면 된다.

                모든 엔드포인트를 웹에 노출
                    application.properties -> management.endpoints.web.exposure.include=*
                특정 엔드포인트를 활성화
                    application.properties -> management.endpoint.{엔드포인트명}.enabled=true

        엔드포인트 목록
            beans : 스프링 컨테이너에 등록된 스프링 빈을 보여준다.
            conditions : condition 을 통해서 빈을 등록할 때 평가 조건과 일치하거나 일치하지 않는 이유를 표시한다.
            configprops : @ConfigurationProperties 를 보여준다.
            env : Environment 정보를 보여준다.
            health : 애플리케이션 헬스 정보를 보여준다.
            httpexchanges : HTTP 호출 응답 정보를 보여준다. HttpExchangeRepository 를 구현한 빈을 별도로 등록해야 한다.
            info : 애플리케이션 정보를 보여준다.
            loggers : 애플리케이션 로거 설정을 보여주고 변경도 할 수 있다.
            metrics : 애플리케이션의 메트릭 정보를 보여준다.
            mappings : @RequestMapping 정보를 보여준다.
            threaddump : 쓰레드 덤프를 실행해서 보여준다.
            shutdown : 애플리케이션을 종료한다. 이 기능은 기본으로 비활성화 되어 있다.

            참고: https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints

        자주 사용하는 엔드포인트
            health(헬스 정보)
                헬스 정보를 사용하면 애플리케이션에 문제가 발생했을 때 문제를 빠르게 인지할 수 있다.
                헬스 정보는 단순히 애플리케이션이 요청에 응답을 할 수 있는지 판단하는 것을 넘어서 애플리케이션이 사용하는 데이터베이스가 응답하는지, 디스크 사용량에는 문제가 없는지 같은 다양한 정보들을 포함해서 만들어진다.
                헬스 정보를 더 자세히 보려면 다음 옵션을 지정하면 된다.
                    application.properties -> management.endpoint.health.show-details=always
                각 헬스 컴포넌트의 상태 정보만 간략하게 노출하려면 다음 옵션을 지정하면 된다.
                    application.properties -> management.endpoint.health.show-components=always
                헬스 컴포넌트 중에 하나라도 문제가 있으면 전체 상태는 DOWN 이 된다.

                참고 - 자세한 헬스 기본 지원 기능
                    https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.health.auto-configured-health-indicators
                참고 - 헬스 기능 직접 구현
                    https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.health.writing-custom-health-indicators

            info(애플리케이션 정보)
                info 엔드포인트는 애플리케이션의 기본 정보를 노출한다.
                기본으로 제공하는 기능들은 다음과 같다.
                    - java : 자바 런타임 정보
                        management.info.java.enabled=true
                    - os : OS 정보
                        management.info.os.enabled=true
                    - env : Environment 에서 info. 로 시작하는 정보
                        management.info.env.enabled=true
                    - build : 빌드 정보, META-INF/build-info.properties 파일이 필요하다.
                        build.gradle -> springBoot { buildInfo() } 추가
                    - git : git 정보, git.properties 파일이 필요하다.
                        build.gradle -> 플러그인 id "com.gorylenko.gradle-git-properties" version "2.4.1"
                        git 에 대한 더 자세한 정보를 보고 싶다면 다음 옵션을 적용하면 된다.
                        application.properties -> management.info.git.mode=full
                env, java, os 는 기본으로 비활성화 되어 있다.
                management.info.{id}.enabled 의 값을 true 로 지정하면 활성화 된다.

            loggers(로거)
                loggers 엔드포인트를 사용하면 로깅과 관련된 정보를 확인하고, 또 실시간으로 변경할 수도 있다.

                실시간 로그 레벨 변경
                    개발 서버는 보통 DEBUG 로그를 사용하지만 운영 서버는 보통 요청이 아주 많다.
                    따라서 로그도 너무 많이 남기 때문에 DEBUG 로그까지 모두 출력하게 되면 성능이나 디스크에 영향을 주게 된다.
                    그래서 운영 서버는 중요하다고 판단되는 INFO 로그 레벨을 사용한다.
                    그런데 서비스 운영중에 문제가 있어서 급하게 DEBUG 나 TRACE 로그를 남겨서 확인해야 확인하고 싶다면 어떻게 해야할까?
                    일반적으로는 로깅 설정을 변경하고, 서버를 다시 시작해야 한다.
                    loggers 엔드포인트를 사용하면 애플리케이션을 다시 시작하지 않고, 실시간으로 로그 레벨을 변경할 수 있다.
                        1. hello.controller 에 LogController.class 작성
                        2. application.properties 에 logging.level.hello.controller=debug 작성
                        3. postman 에서 GET 방식으로 localhost/actuator/loggers/hello.controller 요청
                            응답: {"configuredLevel": "DEBUG", "effectiveLevel": "DEBUG"}
                        4. postman 에서 POST 방식으로 body 에 JSON 데이터 {"configuredLevel": "TRACE"} 를 담은 후 localhost/actuator/loggers/hello.controller 요청
                        5. postman 에서 GET 방식으로 localhost/actuator/loggers/hello.controller 요청
                            응답: {"configuredLevel": "TRACE", "effectiveLevel": "TRACE"}

            httpexchanges(HTTP 요청 응답 기록)
                HTTP 요청과 응답의 과거 기록을 확인하고 싶다면 httpexchanges 엔드포인트를 사용하면 된다.
                HttpExchangeRepository 인터페이스의 구현체를 빈으로 등록하면 httpexchanges 엔드포인트를 사용할 수 있다.
                해당 빈을 등록하지 않으면 httpexchanges 엔드포인트가 활성화 되지 않는다.(ActuatorApplication.class 에 빈으로 등록)
                스프링 부트는 기본으로 InMemoryHttpExchangeRepository 구현체를 제공한다.
                이 구현체는 최대 100개의 HTTP 요청을 제공한다. 최대 요청이 넘어가면 과거 요청을 삭제한다.
                setCapacity()로 최대 요청수를 변경할 수 있다.

                참고로 이 기능은 매우 단순하고 기능에 제한이 많기 때문에 개발 단계에서만 사용하고 실제 운영 서비스에서는 모니터링 툴이나 핀포인트, Zipkin 같은 다른 기술을 사용하는 것이 좋다.

    액츄에이터와 보안
        액츄에이터가 제공하는 기능들은 우리 애플리케이션의 내부 정보를 너무 많이 노출한다.
        그래서 외부 인터넷 망이 공개된 곳에 액츄에이터의 엔드포인트를 공개하는 것은 보안상 좋은 방안이 아니다.
        액츄에이터의 엔드포인트들은 외부 인터넷에서 접근이 불가능하게 막고, 내부에서만 접근 가능한 내부망을 사용하는 것이 안전하다.

        액츄에이터를 다른 포트에서 실행
            예를 들어서 외부 인터넷 망을 통해서 8080 포트에만 접근할 수 있고 다른 포트는 내부망에서만 접근할 수 있다면 액츄에이터에 다른 포트를 설정하면 된다.
            액츄에이터의 기능을 애플리케이션 서버와는 다른 포트에서 실행하려면 다음과 같이 설정하면 된다. 이 경우 기존 8080 포트에서는 액츄에이터를 접근할 수 없다.
                application.properties -> management.server.port=9292

        액츄에이터 URL 경로에 인증 설정
            포트를 분리하는 것이 어렵고 어쩔 수 없이 외부 인터넷 망을 통해서 접근해야 한다면 /actuator 경로에 서블릿 필터 또는 스프링 시큐티리를 통해서 인증된 사용자만 접근 가능하도록 추가 개발이 필요하다.
            엔드포인트의 기본 경로를 변경하려면 다음과 같이 설정하면 된다.
                application.properties -> management.endpoints.web.base-path=/manage
    */
}
