package hello;

public class A {
    /*
    WAR 배포 방식의 단점
        웹 애플리케이션을 개발하고 배포하려면 다음과 같은 과정을 거쳐야 한다.
            - 톰캣 같은 웹 애플리케이션 서버(WAS)를 별도로 설치해야 한다.
            - 애플리케이션 코드를 WAR 로 빌드해야 한다.
            - 빌드한 WAR 파일을 WAS 에 배포해야 한다.

        웹 애플리케이션을 구동하고 싶으면 웹 애플리케이션 서버를 별도로 설치해야 하는 구조이다.
        과거에는 이렇게 웹 애플리케이션 서버와 웹 애플리케이션 빌드 파일(WAR)이 분리되어 있는것이 당연한 구조였다.
        그런데 이런 방식은 다음과 같은 단점이 있다.

        단점
            - 톰캣 같은 WAS 를 별도로 설치해야 한다.
            - 개발 환경 설정이 복잡하다.
                - 단순한 자바라면 별도의 설정을 고민하지 않고, main() 메서드만 실행하면 된다.
                - 웹 애플리케이션은 WAS 실행하고 또 WAR 와 연동하기 위한 복잡한 설정이 들어간다.
            - 배포 과정이 복잡하다. WAR 를 만들고 이것을 또 WAS 에 전달해서 배포해야 한다.
            - 톰캣의 버전을 변경하려면 톰캣을 다시 설치해야 한다.

        고민
            누군가는 오래전부터 이런 방식의 불편함을 고민해왔다.
            단순히 자바의 main() 메서드만 실행하면 웹 서버까지 같이 실행되도록 하면 되지 않을까?
            톰캣도 자바로 만들어져 있으니 톰캣을 마치 하나의 라이브러리 처럼 포함해서 사용해도 되지 않을까?
            쉽게 이야기해서 톰캣 같은 웹서버를 라이브러리로 내장해버리는 것이다.
            이런 문제를 해결하기 위해 톰캣을 라이브러리로 제공하는 내장 톰캣(embed tomcat) 기능을 제공한다.
    */

    /*
    빌드와 배포
        자바의 main() 메서드를 실행하기 위해서는 jar 형식으로 빌드해야 한다.
        그리고 jar 안에는 META-INF/MANIFEST.MF 파일에 실행할 main() 메서드의 클래스를 지정해주어야 한다.
        <META-INF/MANIFEST.MF>
            Manifest-Version: 1.0
            Main-Class: hello.embed.EmbedTomcatSpringMain

        <build.gradle - buildJar> - Gradle 의 도움을 받아 위 과정을 쉽게 진행할 수 있다.
            tasks.register('buildJar', Jar) {
                manifest {
                    attributes 'Main-Class': 'hello.embed.EmbedTomcatSpringMain'
                }
                with jar
            }

        프로젝트 위치로 가서 명령 프롬프트를 열고 jar 를 빌드한다.
            gradlew clean buildJar
        그러면 build/libs/embed-0.0.1-SNAPSHOT.jar 가 생성된다.
        jar 파일이 있는 폴더로 이동한 후에 jar 를 실행한다.
            java -jar embed-0.0.1-SNAPSHOT.jar
        실행을 하게 되면
            Error: Unable to initialize main class hello.embed.EmbedTomcatSpringMain
            Caused by: java.lang.NoClassDefFoundError: org/springframework/web/context/WebApplicationContext
        에러가 발생한다. 오류 메시지를 잘 읽어보면 스프링 관련 클래스를 찾을 수 없다는 오류이다.
        무엇이 문제일까?
            jar 압축을 풀어보면 구조가 아래와 같다.
                - META-INF
                    - MANIFEST.MF
                - hello
                    - servlet
                        - HelloServlet.class
                    - embed
                        - EmbedTomcatSpringMain.class
                        - EmbedTomcatServletMain.class
                    - spring
                        - HelloConfig.class
                        - HelloController.class
            jar 를 푼 결과를 보면 스프링 라이브러리나 내장 톰캣 라이브러리가 전혀 보이지 않는다.
            따라서 해당 오류가 발생한것이다.

            jar 파일은 jar 파일을 포함할 수 없다.
                - WAR 와 다르게 JAR 파일은 내부에 라이브러리 역할을 하는 JAR 파일을 포함할 수 없다.
                  포함한다고 해도 인식이 안된다. 이것이 JAR 파일 스펙의 한계이다.
                  그렇다고 WAR 를 사용할 수 도 없다. WAR 는 웹 애플리케이션 서버(WAS) 위에서만 실행할 수 있다.

                - 대안으로는 라이브러리 jar 파일을 모두 구해서 MANIFEST 파일에 해당 경로를 적어주면 인식이 되지만 매우 번거롭다.
                  또한 Jar 파일안에 Jar 파일을 포함할 수 없기 때문에 라이브러리 역할을 하는 jar 파일도 항상 함께 가지고 다녀야 한다.
                  이 방법은 권장하기 않는다.

        대안으로는 fat jar 또는 uber jar 라고 불리는 방법이다.
        Jar 안에는 Jar 를 포함할 수 없다. 하지만 클래스는 얼마든지 포함할 수 있다.
        라이브러리에 사용되는 jar 를 풀면 class 들이 나온다. 이 class 를 뽑아서 새로 만드는 jar 에 포함하는 것이다.
        이렇게 하면 수 많은 라이브러리에서 나오는 class 때문에 뚱뚱한(fat) jar 가 탄생한다. 그래서 Fat Jar 라고 부른다.

        <build.gradle - buildFatJar>
            tasks.register('buildFatJar', Jar) {
                manifest {
                    attributes 'Main-Class': 'hello.embed.EmbedTomcatSpringMain'
                }
                duplicatesStrategy = DuplicatesStrategy.WARN
                from { configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
                with jar
            }

        프로젝트 위치로 가서 명령 프롬프트를 열고 jar 를 빌드한다.
            gradlew clean buildFatJar
        그러면 build/libs/embed-0.0.1-SNAPSHOT.jar 가 생성된다.
        jar 파일이 있는 폴더로 이동한 후에 jar 를 실행한다.
            java -jar embed-0.0.1-SNAPSHOT.jar
        요번에는 정상적으로 실행이 된다.
        Jar 를 풀어보면 우리가 만든 클래스를 포함해서, 수 많은 라이브러리에서 제공되는 클래스들이 포함되어 있는 것을 확인할 수 있다.

        Fat Jar 의 장점
            - Fat Jar 덕분에 하나의 jar 파일에 필요한 라이브러리들을 내장할 수 있게 되었다.
            - 내장 톰캣 라이브러리를 jar 내부에 내장할 수 있게 되었다.
            - 덕분에 하나의 jar 파일로 배포부터, 웹 서버 설치+실행까지 모든 것을 단순화 할 수 있다.

        WAR 단점과 해결
            - 톰캣 같은 WAS 를 별도로 설치해야 한다.
                - 해결: WAS 를 별도로 설치하지 않아도 된다. 톰캣 같은 WAS 가 라이브러리로 jar 내부에 포함되어 있다.
            - 개발 환경 설정이 복잡하다.
                - 단순한 자바라면 별도의 설정을 고민하지 않고, main() 메서드만 실행하면 된다.
                - 웹 애플리케이션은 WAS 를 연동하기 위한 복잡한 설정이 들어간다.
                - 해결: IDE 에 복잡한 WAS 설정이 필요하지 않다. 단순히 main() 메서드만 실행하면 된다.
            - 배포 과정이 복잡하다. WAR 를 만들고 이것을 또 WAS 에 전달해서 배포해야 한다.
                - 해결: 배포 과정이 단순하다. JAR 를 만들고 이것을 원하는 위치에서 실행만 하면 된다.
            - 톰캣의 버전을 업데이트 하려면 톰캣을 다시 설치해야 한다.
                - 해결: gradle 에서 내장 톰캣 라이브러리 버전만 변경하고 빌드 후 실행하면 된다.

        Fat Jar 의 단점
            - 어떤 라이브러리가 포함되어 있는지 확인하기 어렵다.
                - 모두 class 로 풀려있으니 어떤 라이브러리가 사용되고 있는지 추적하기 어렵다.
            - 파일명 중복을 해결할 수 없다.
                - 클래스나 리소스 명이 같은 경우 하나를 포기해야 한다. 이것은 심각한 문제를 발생한다.
                  예를 들어서 서블릿 컨테이너 초기화에서 학습한 부분을 떠올려 보자.
                - META-INF/services/jakarta.servlet.ServletContainerInitializer 이 파일이 여러 라이브러리(jar)에 있을 수 있다.
                - A 라이브러리와 B 라이브러리 둘다 해당 파일을 사용해서 서블릿 컨테이너 초기화를 시도한다.
                  둘 다 해당 파일을 jar 안에 포함한다.
                - Fat Jar 를 만들면 파일명이 같으므로 A, B 라이브러리가 둘다 가지고 있는 파일 중에 하나의 파일만 선택된다.
                  결과적으로 나머지 하나는 포함되지 않으므로 정상 동작하지 않는다.
    */
}
