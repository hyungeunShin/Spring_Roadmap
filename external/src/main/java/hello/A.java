package hello;

public class A {
    /*
    외부 설정이란?
        하나의 애플리케이션을 여러 다른 환경에서 사용해야 할 때가 있다.
        대표적으로 개발이 잘 진행되고 있는지 내부에서 확인하는 용도의 개발 환경, 그리고 실제 고객에게 서비스하는 운영 환경이 있다.
            - 개발 환경: 개발 서버, 개발 DB 사용
            - 운영 환경: 운영 서버, 운영 DB 사용

        문제는 각각의 환경에 따라서 서로 다른 설정값이 존재한다는 점이다.
        예를 들어서 애플리케이션이 개발 DB에 접근하려면 dev.db.com 이라는 url 정보가 필요한데, 운영 DB에 접근하려면 prod.db.com 이라는 서로 다른 url 을 사용해야 한다.
        이 문제를 해결하는 가장 단순한 방법은 다음과 같이 각각의 환경에 맞게 애플리케이션을 빌드하는 것이다.
            - 개발 환경에는 dev.db.com 이 필요하므로 이 값을 애플리케이션 코드에 넣은 다음에 빌드해서 개발 app.jar 를 만든다.
            - 운영 환경에는 prod.db.com 이 필요하므로 이 값을 애플리케이션 코드에 넣은 다음에 빌드해서 운영 app.jar 를 만든다.
        이렇게 하면 각각의 환경에 맞는 개발 app.jar , 운영 app.jar 가 만들어지므로 해당 파일들을 각 환경별로 배포하면 된다.

        하지만 이것은 다음과 이유로 좋은 방법이 아니다.
            - 환경에 따라서 빌드를 여러번 해야 한다.
            - 개발 버전과 운영 버전의 빌드 결과물이 다르다.
              따라서 개발 환경에서 검증이 되더라도 운영 환경에서 다른 빌드 결과를 사용하기 때문에 예상치 못한 문제가 발생할 수 있다.
              개발용 빌드가 끝나고 검증한 다음에 운영용 빌드를 해야 하는데 그 사이에 누군가 다른 코드를 변경할 수도 있다.
              한마디로 진짜 같은 소스코드에서 나온 결과물인지 검증하기가 어렵다.
            - 각 환경에 맞추어 최종 빌드가 되어 나온 빌드 결과물은 다른 환경에서 사용할 수 없어서 유연성이 떨어진다.
              향후 다른 환경이 필요하면 그곳에 맞도록 또 빌드를 해야 한다.

        그래서 보통 다음과 같이 빌드는 한번만 하고 각 환경에 맞추어 실행 시점에 외부 설정값을 주입한다.
            - 배포 환경과 무관하게 하나의 빌드 결과물을 만든다. 여기서는 app.jar 를 빌드한다. 이 안에는 설정값을 두지 않는다.
            - 설정값은 실행 시점에 각 환경에 따라 외부에서 주입한다.
                - 개발 서버: app.jar 를 실행할 때 dev.db.com 값을 외부 설정으로 주입한다.
                - 운영 서버: app.jar 를 실행할 때 prod.db.com 값을 외부 설정으로 주입한다.

        이렇게 하면 빌드도 한번만 하면 되고, 개발 버전과 운영 버전의 빌드 결과물이 같기 때문에 개발환경에서 검증되면 운영 환경에서도 믿고 사용할 수 있다.
        그리고 이후에 새로운 환경이 추가되어도 별도의 빌드 과정 없이 기존 app.jar 를 사용해서 손쉽게 새로운 환경을 추가할 수 있다.

        유지보수하기 좋은 애플리케이션 개발의 가장 기본 원칙은 변하는 것과 변하지 않는 것을 분리하는 것이다.
        유지보수하기 좋은 애플리케이션을 개발하는 단순하면서도 중요한 원칙은 변하는 것과 변하지 않는 것을 분리하는 것이다.
        각 환경에 따라 변하는 외부 설정값은 분리하고, 변하지 않는 코드와 빌드 결과물은 유지했다.
        덕분에 빌드 과정을 줄이고, 환경에 따른 유연성을 확보하게 되었다.

    애플리케이션을 실행할 때 필요한 설정값을 외부에서 어떻게 불러와서 애플리케이션에 전달할 수 있을까?
    외부 설정은 일반적으로 다음 4가지 방법이 있다.
        - OS 환경 변수: OS 에서 지원하는 외부 설정, 해당 OS를 사용하는 모든 프로세스에서 사용
        - 자바 시스템 속성: 자바에서 지원하는 외부 설정, 해당 JVM 안에서 사용
        - 자바 커맨드 라인 인수: 커맨드 라인에서 전달하는 외부 설정, 실행시 main(args) 메서드에서 사용
        - 외부 파일(설정 데이터): 프로그램에서 외부 파일을 직접 읽어서 사용
            - 애플리케이션에서 특정 위치의 파일을 읽도록 해둔다. 예) data/hello.txt
            - 그리고 각 서버마다 해당 파일안에 다른 설정 정보를 남겨둔다.
                - 개발 서버 hello.txt : url=dev.db.com
                - 운영 서버 hello.txt : url=prod.db.com

    외부 설정 - 커맨드 라인 옵션 인수와 스프링 부트
        스프링 부트는 커맨드 라인을 포함해서 커맨드 라인 옵션 인수를 활용할 수 있는 ApplicationArguments 를 스프링 빈으로 등록해둔다.
        그리고 그 안에 입력한 커맨드 라인을 저장해둔다.
        그래서 해당 빈을 주입 받으면 커맨드 라인으로 입력한 값을 어디서든 사용할 수 있다.
        CommandLineBean.class 참고

    외부 설정 - 스프링 통합
        지금까지 살펴본 커맨드 라인 옵션 인수, 자바 시스템 속성, OS 환경변수는 모두 외부 설정을 key=value 형식으로 사용할 수 있는 방법이다.
        그런데 이 외부 설정값을 읽어서 사용하는 개발자 입장에서 단순하게 생각해보면 모두 key=value 형식이고 설정값을 외부로 뽑아둔 것이다.
        그런데 어디에 있는 외부 설정값을 읽어야 하는지에 따라서 각각 읽는 방법이 다르다는 단점이 있다.

        예를 들어서 OS 환경 변수에 두면 System.getenv(key) 를 사용해야 하고, 자바 시스템 속성을 사용하면 System.getProperty(key) 를 사용해야 한다.
        만약 OS에 환경 변수를 두었는데 이후에 정책이 변경되어서 자바 시스템 속성에 환경 변수를 두기로 했다고 가정해보자.
        그러면 해당 코드들을 모두 변경해야 한다.

        외부 설정값이 어디에 위치하든 상관없이 일관성 있고 편리하게 key=value 형식의 외부 설정값을 읽을 수 있으면 사용하는 개발자 입장에서 더 편리하다.
        또한 외부 설정값을 설정하는 방법도 더 유연해질 수 있다.
        예를 들어서 외부 설정 값을 OS 환경변수를 사용하다가 자바 시스템 속성으로 변경하는 경우에 소스코드를 다시 빌드하지 않고 그대로 사용할 수 있다.

        스프링은 이 문제를 Environment 와 PropertySource 라는 추상화를 통해서 해결한다.

    PropertySource
        - org.springframework.core.env.PropertySource
        - 스프링은 PropertySource 라는 추상 클래스를 제공하고 각각의 외부 설정를 조회하는 XxxPropertySource 구현체를 만들어두었다.
            예) CommandLinePropertySource, SystemEnvironmentPropertySource
        - 스프링은 로딩 시점에 필요한 PropertySource 들을 생성하고 Environment 에서 사용할 수 있게 연결해둔다.

    Environment
        - org.springframework.core.env.Environment
        - Environment 를 통해서 특정 외부 설정에 종속되지 않고 일관성 있게 key=value 형식의 외부 설정에 접근할 수 있다.
            - environment.getProperty(key) 를 통해서 값을 조회할 수 있다.
            - Environment 는 내부에서 여러 과정을 거쳐서 PropertySource 들에 접근한다.
            - 같은 값이 있을 경우를 대비해서 스프링은 미리 우선순위를 정해두었다.
        - 모든 외부 설정은 이제 Environment 를 통해서 조회하면 된다.

    설정 데이터(파일)
        application.properties, application.yml 도 PropertySource 에 추가된다.
        따라서 Environment 를 통해서 접근할 수 있다.

    설정 데이터1 - 외부 파일
        지금까지 학습한 OS 환경 변수, 자바 시스템 속성, 커맨드 라인 옵션 인수는 사용해야 하는 값이 늘어날수록 사용하기가 불편해진다.
        실무에서는 수십개의 설정값을 사용하기도 하므로 이런 값들을 프로그램을 실행할 때 마다 입력하게 되면 번거롭고 관리도 어렵다.
        그래서 등장하는 대안으로는 설정값을 파일에 넣어서 관리하는 방법이다.
        그리고 애플리케이션 로딩 시점에 해당 파일을 읽어들이면 된다.
        그 중에서도 .properties 라는 파일은 key=value 형식을 사용해서 설정값을 관리하기에 아주 적합하다.

        스프링과 설정 데이터(여기에 application.properties 는 resources/application.properties 가 아니다.)
            개발자가 파일을 읽어서 설정값으로 사용할 수 있도록 개발을 해야겠지만 스프링 부트는 이미 이런 부분을 다 구현해두었다.
            개발자는 application.properties 라는 이름의 파일을 자바를 실행하는 위치에 만들어 두기만 하면 된다.
            그러면 스프링이 해당 파일을 읽어서 사용할 수 있는 PropertySource 의 구현체를 제공한다.
            스프링에서는 이러한 application.properties 파일을 설정 데이터(Config data)라 한다.
            당연히 설정 데이터도 Environment 를 통해서 조회할 수 있다.

            확인
                1. gradlew clean build
                2. build/libs 로 이동 후 application.properties 생성
                    url=dev.db.com
                    username=dev_user
                    password=dev_pw
                3. java -jar external-0.0.1-SNAPSHOT.jar 실행
        남은 문제
            - 외부 설정을 별도의 파일로 관리하게 되면 설정 파일 자체를 관리하기 번거로운 문제가 발생한다.
            - 서버가 10대면 변경사항이 있을 때 10대 서버의 설정 파일을 모두 각각 변경해야 하는 불편함이 있다.
            - 설정 파일이 별도로 관리되기 때문에 설정값의 변경 이력을 확인하기 어렵다.
              특히 설정값의 변경 이력이 프로젝트 코드들과 어떻게 영향을 주고 받는지 그 이력을 같이 확인하기 어렵다.

    설정 데이터2 - 내부 파일 분리
        설정 파일을 외부에 관리하는 것은 상당히 번거로운 일이다.
        설정을 변경할 때 마다 서버에 들어가서 각각의 변경 사항을 수정해두어야 한다.
        이 문제를 해결하는 간단한 방법은 설정 파일을 프로젝트 내부에 포함해서 관리하는 것이다.
        그리고 빌드 시점에 함께 빌드되게 하는 것이다.
        이렇게 하면 애플리케이션을 배포할 때 설정 파일의 변경 사항도 함께 배포할 수 있다.
        쉽게 이야기해서 jar 하나로 설정 데이터까지 포함해서 관리하는 것이다.

        0. 프로젝트 안에 소스 코드 뿐만 아니라 각 환경에 필요한 설정 데이터도 함께 포함해서 관리한다.
            - 개발용 설정 파일: application-dev.properties
            - 운영용 설정 파일: application-prod.properties
        1. 빌드 시점에 개발, 운영 설정 파일을 모두 포함해서 빌드한다.
        2. app.jar 는 개발, 운영 두 설정 파일을 모두 가지고 배포된다.
        3. 실행할 때 어떤 설정 데이터를 읽어야 할지 최소한의 구분은 필요하다.
            - 개발 환경이라면 application-dev.properties 를 읽어야 한다.
            - 운영 환경이라면 application-prod.properties 를 읽어야 한다.
            - 실행할 때 외부 설정을 사용해서 개발 서버는 dev 라는 값을 제공하고, 운영 서버는 prod 라는 값을 제공하자. 편의상 이 값을 프로필이라 하자.

        외부 설정으로 넘어온 프로필 값이 dev 라면 application-dev.properties 를 읽고 prod 라면 application-prod.properties 를 읽어서 사용하면 된다.
        스프링은 이미 설정 데이터를 내부에 파일로 분리해두고 외부 설정값(프로필)에 따라 각각 다른 파일을 읽는 방법을 다 구현해두었다.

        프로필
            스프링은 이런 곳에서 사용하기 위해 프로필이라는 개념을 지원한다.
            spring.profiles.active 외부 설정에 값을 넣으면 해당 프로필을 사용한다고 판단한다.
            그리고 프로필에 따라서 application-{profile}.properties 규칙으로 해당 프로필에 맞는 내부 파일(설정 데이터)을 조회한다.
            예)
                - spring.profiles.active=dev
                    dev 프로필이 활성화 되었다.
                    application-dev.properties 를 설정 데이터로 사용한다.
                - spring.profiles.active=prod
                    prod 프로필이 활성화 되었다.
                    application-prod.properties 를 설정 데이터로 사용한다.

        확인
            1. main/resources 에 application-dev.properties 와 application-prod.properties 추가
            2. gradlew clean build
            3. build/libs 로 이동 후 실행
                java -Dspring.profiles.active=dev -jar external-0.0.1-SNAPSHOT.jar
                java -jar external-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

        남은 문제
            - 설정 파일을 각각 분리해서 관리하면 한눈에 전체가 들어오지 않는 단점이 있다.

    설정 데이터3 - 내부 파일 합체
        설정 파일을 각각 분리해서 관리하면 한눈에 전체가 들어오지 않는 단점이 있다.
        스프링은 이런 단점을 보완하기 위해 물리적인 하나의 파일 안에서 논리적으로 영역을 구분하는 방법을 제공한다.

        - 기존에는 dev 환경은 application-dev.properties, prod 환경은 application-prod.properties 파일이 필요했다.
        - 스프링은 하나의 application.properties 파일 안에서 논리적으로 영역을 구분하는 방법을 제공한다.
        - application.properties 라는 하나의 파일 안에서 논리적으로 영역을 나눌 수 있다.
            - application.properties 구분 방법 #--- 또는 !--- (dash 3)
            - application.yml 구분 방법 --- (dash 3)
            - 구분 기호 바로 앞과 뒤의 줄은 같은 주석 접두사가 아니어야 한다.
        - 프로필에 따라 논리적으로 구분된 설정 데이터를 활성화 하는 방법
            - spring.config.activate.on-profile 에 프로필 값 지정

        확인
            1. application-dev.properties 와 application-prod.properties 주석 처리
            2. application.properties 추가
            3. gradlew clean build
            4. build/libs 로 이동 후 실행
                java -Dspring.profiles.active=dev -jar external-0.0.1-SNAPSHOT.jar
                java -jar external-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

    우선순위
        설정데이터
            - 단순하게 문서를 위에서 아래로 순서대로 읽으면서 값을 설정한다. 이때 기존 데이터가 있으면 덮어쓴다.
            - 논리 문서에 spring.config.activate.on-profile 옵션이 있으면 해당 프로필을 사용할 때만 논리 문서를 적용한다.
        전체
            ★ 우선순위는 위에서 아래로 적용된다. 아래가 더 우선순위가 높다.

            자주 사용하는 우선순위
                - 설정 데이터(application.properties)
                - OS 환경변수
                - 자바 시스템 속성
                - 커맨드 라인 옵션 인수
                - @TestPropertySource (테스트에서 사용)

                참고 - https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config

            설정 데이터 우선순위
                - jar 내부 application.properties
                - jar 내부 프로필 적용 파일 application-{profile}.properties
                - jar 외부 application.properties
                - jar 외부 프로필 적용 파일 application-{profile}.properties

                참고 - https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files

        정리
            이렇게 우선순위에 따라서 설정을 추가하거나 변경하는 방식은 상당히 편리하면서도 유연한 구조를 만들어준다.
            실무에서 대부분의 개발자들은 application.properties 에 외부 설정값들을 보관한다.
            이렇게 설정 데이터를 기본으로 사용하다가 일부 속성을 변경할 필요가 있다면 더 높은 우선순위를 가지는 자바 시스템 속성이나 커맨드 라인 옵션 인수를 사용하면 되는 것이다.
            또는 기본적으로 application.properties 를 jar 내부에 내장하고 있다가 특별한 환경에서는 application.properties 를 외부 파일로 새로 만들고 변경하고 싶은 일부 속성만 입력해서 변경하는 것도 가능하다.
    */
}
