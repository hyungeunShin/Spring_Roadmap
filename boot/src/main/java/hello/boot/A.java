package hello.boot;

public class A {
    /*
    빌드와 배포
        프로젝트 위치로 가서 명령 프롬프트를 열고 jar 를 빌드한다.
            gradlew clean build
        그러면 build/libs/boot-0.0.1-SNAPSHOT.jar 가 생성된다.
            ※ 참고 - 빌드 결과를 보면 boot-0.0.1-SNAPSHOT-plain.jar 파일도 보이는데, 이것은 우리가 개발한 코드만 순수한 jar 로 빌드한 것이다.
        jar 파일이 있는 폴더로 이동한 후에 jar 를 실행한다.
            java -jar boot-0.0.1-SNAPSHOT.jar
        실행을 하게 되면 성공적으로 실행된다.

        스프링 부트 jar 구조
            - META-INF
                - MANIFEST.MF
            - org/springframework/boot/loader
                - JarLauncher.class : 스프링 부트 main() 실행 클래스
            - BOOT-INF
                - classes : 우리가 개발한 class 파일과 리소스 파일
                    - hello/boot/BootApplication.class
                    - hello/boot/controller/HelloController.class
                    - ...
                - lib : 외부 라이브러리
                    - spring-webmvc-6.1.10.jar
                    - tomcat-embed-core-10.1.25.jar
                    - ...
                - classpath.idx : 외부 라이브러리 경로
                - layers.idx : 스프링 부트 구조 경로

        JAR 를 푼 결과를 보면 Fat Jar 가 아니라 처음보는 새로운 구조로 만들어져 있다.
        심지어 jar 내부에 jar 를 담아서 인식하는 것이 불가능한데, jar 가 포함되어 있고, 인식까지 되었다.

    실행 가능 Jar
        스프링 부트는 FatJar 문제를 해결하기 위해 jar 내부에 jar 를 포함할 수 있는 특별한 구조의 jar 를 만들었다.
        동시에 만든 jar 를 내부 jar 를 포함해서 실행할 수 있게 했다.
        이것을 실행 가능 Jar(Executable Jar)라 한다.
        이 실행 가능 Jar 를 사용하면 다음 문제들을 깔끔하게 해결할 수 있다.
            - 문제: 어떤 라이브러리가 포함되어 있는지 확인하기 어렵다.
                - 해결: jar 내부에 jar 를 포함하기 때문에 어떤 라이브러리가 포함되어 있는지 쉽게 확인할 수 있다.
            - 문제: 파일명 중복을 해결할 수 없다.
                - 해결: jar 내부에 jar 를 포함하기 때문에 a.jar , b.jar 내부에 같은 경로의 파일이 있어도 둘다 인식할 수 있다.

        참고로 실행 가능 Jar 는 자바 표준은 아니고, 스프링 부트에서 새롭게 정의한 것이다.

    Jar 실행 정보
        java -jar xxx.jar 를 실행하게 되면 우선 META-INF/MANIFEST.MF 파일을 찾는다.
        그리고 여기에 있는 Main-Class 를 읽어서 main() 메서드를 실행하게 된다.
        <META-INF/MANIFEST.MF>
        Manifest-Version: 1.0
        Main-Class: org.springframework.boot.loader.launch.JarLauncher
        Start-Class: hello.boot.BootApplication
        Spring-Boot-Version: 3.3.1
        Spring-Boot-Classes: BOOT-INF/classes/
        Spring-Boot-Lib: BOOT-INF/lib/
        Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
        Spring-Boot-Layers-Index: BOOT-INF/layers.idx
        Build-Jdk-Spec: 17
        Implementation-Title: boot
        Implementation-Version: 0.0.1-SNAPSHOT

        - Main-Class
            - 우리가 기대한 main() 이 있는 hello.boot.BootApplication 이 아니라 JarLauncher 라는 전혀 다른 클래스를 실행하고 있다.
            - JarLauncher 는 스프링 부트가 빌드시에 넣어준다. org/springframework/boot/loader/JarLauncher 에 실제로 포함되어 있다.
            - 스프링 부트는 jar 내부에 jar 를 읽어들이는 기능이 필요하다. 또 특별한 구조에 맞게 클래스 정보도 읽어들여야 한다.
              바로 JarLauncher 가 이런 일을 처리해준다. 이런 작업을 먼저 처리한 다음 Start-Class: 에 지정된 main() 을 호출한다.
       -  Start-Class : 우리가 기대한 main() 이 있는 hello.boot.BootApplication 가 적혀있다.
        - 기타: 스프링 부트가 내부에서 사용하는 정보들이다.
            - Spring-Boot-Version : 스프링 부트 버전
            - Spring-Boot-Classes : 개발한 클래스 경로
            - Spring-Boot-Lib : 라이브러리 경로
            - Spring-Boot-Classpath-Index : 외부 라이브러리 모음
            - Spring-Boot-Layers-Index : 스프링 부트 구조 정보
        - 참고: Main-Class 를 제외한 나머지는 자바 표준이 아니다. 스프링 부트가 임의로 사용하는 정보이다.

    스프링 부트 로더
        org/springframework/boot/loader 하위에 있는 클래스들이다.
        JarLauncher 를 포함한 스프링 부트가 제공하는 실행 가능 Jar 를 실제로 구동시키는 클래스들이 포함되어 있다.
        스프링 부트는 빌드시에 이 클래스들을 포함해서 만들어준다.

    BOOT-INF
        - classes : 우리가 개발한 class 파일과 리소스 파일
        - lib : 외부 라이브러리
        - classpath.idx : 외부 라이브러리 모음
        - layers.idx : 스프링 부트 구조 정보
        - WAR 구조는 WEB-INF 라는 내부 폴더에 사용자 클래스와 라이브러리를 포함하고 있는데, 실행 가능 Jar 도 그 구조를 본따서 만들었다.
          이름도 유사하게 BOOT-INF 이다.
        - JarLauncher 를 통해서 여기에 있는 classes 와 lib 에 있는 jar 파일들을 읽어들인다.

    실행 과정 정리
        1. java -jar xxx.jar
        2. MANIFEST.MF 인식
        3. JarLauncher.main() 실행
            - BOOT-INF/classes/ 인식
            - BOOT-INF/lib/ 인식
        4. BootApplication.main() 실행
    */
}
