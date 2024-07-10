package hello;

public class A {
    /*
    외장 서버 VS 내장 서버
        전통적인 방식
            과거에 자바로 웹 애플리케이션을 개발할 때는 먼저 서버에 톰캣 같은 WAS(웹 애플리케이션 서버)를 설치했다.
            그리고 WAS 에서 동작하도록 서블릿 스펙에 맞추어 코드를 작성하고 WAR 형식으로 빌드해서 war 파일을 만들었다.
            이렇게 만들어진 war 파일을 WAS 에 전달해서 배포하는 방식으로 전체 개발 주기가 동작했다.
            이런 방식은 WAS 기반 위에서 개발하고 실행해야 한다.
            IDE 같은 개발 환경에서도 WAS 와 연동해서 실행되도록 복잡한 추가 설정이 필요하다.

        최근 방식
            최근에는 스프링 부트가 내장 톰캣을 포함하고 있다.
            애플리케이션 코드 안에 톰캣 같은 WAS 가 라이브러리로 내장되어 있다는 뜻이다.
            개발자는 코드를 작성하고 JAR 로 빌드한 다음에 해당 JAR 를 원하는 위치에서 실행하기만 하면 WAS 도 함께 실행된다.
            쉽게 이야기해서 개발자는 main() 메서드만 실행하면 되고, WAS 설치나 IDE 같은 개발 환경에서 WAS 와 연동하는 복잡한 일은 수행하지 않아도 된다.

    JAR 소개
        자바는 여러 클래스와 리소스를 묶어서 JAR (Java Archive)라고 하는 압축 파일을 만들 수 있다.
        이 파일은 JVM 위에서 직접 실행되거나 또는 다른 곳에서 사용하는 라이브러리로 제공된다.
        직접 실행하는 경우 main() 메서드가 필요하고, MANIFEST.MF 파일에 실행할 메인 메서드가 있는 클래스를 지정해두어야 한다.

        실행 예) java -jar abc.jar
        Jar 는 쉽게 이야기해서 클래스와 관련 리소스를 압축한 단순한 파일이다.
        필요한 경우 이 파일을 직접 실행할 수도 있고, 다른 곳에서 라이브러리로 사용할 수도 있다.

    WAR 소개
        WAR(Web Application Archive)라는 이름에서 알 수 있듯 WAR 파일은 웹 애플리케이션 서버(WAS)에 배포할 때 사용하는 파일이다.
        JAR 파일이 JVM 위에서 실행된다면, WAR 는 웹 애플리케이션 서버 위에서 실행된다.
        웹 애플리케이션 서버 위에서 실행되고, HTML 같은 정적 리소스와 클래스 파일을 모두 함께 포함하기 때문에 JAR 와 비교해서 구조가 더 복잡하다.
        그리고 WAR 구조를 지켜야 한다.

        구조
            - WEB-INF
                - classes : 실행 클래스 모음
                - lib : 라이브러리 모음
                - web.xml : 웹 서버 배치 설정 파일(생략 가능)
            - index.html : 정적 리소스

            - WEB-INF 폴더 하위는 자바 클래스와 라이브러리, 그리고 설정 정보가 들어가는 곳이다.
            - WEB-INF 를 제외한 나머지 영역은 HTML, CSS 같은 정적 리소스가 사용되는 영역이다.

    WAR 빌드
        1. 프로젝트 폴더로 이동
        2. 명령 프롬프트 실행
            gradlew build
        3. WAR 파일 생성 확인
            build/libs/server-0.0.1-SNAPSHOT.war

    WAR 배포
        1. 톰캣 서버 종료
            shutdown.bat
        2. 톰캣폴더/webapps 하위 모두 삭제
        3. 빌드된 server-0.0.1-SNAPSHOT.war 복사
        4. 톰캣폴더/webapps 하위에 복사
            톰캣폴더/webapps/server-0.0.1-SNAPSHOT.war
        5. 이름 변경
            톰캣폴더/webapps/ROOT.war
        6. 톰캣 서버 실행
            startup.bat
    */
}