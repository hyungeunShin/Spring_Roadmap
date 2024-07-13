package hello.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootApplication {
	/*
	내장 톰캣 의존관계 확인
		spring-boot-starter-web 를 사용하면 내부에서 내장 톰캣을 사용한다.
		라이브러리 의존관계를 따라가보면 내장 톰캣(tomcat-embed-core)이 포함된 것을 확인할 수 있다.

	스프링 부트 내부에서 스프링 컨테이너를 생성하는 코드
		org.springframework.boot.web.servlet.context.ServletWebServerApplicationContextFactory 클래스 createContext() 메소드 확인
		- return new AnnotationConfigServletWebServerApplicationContext(); 이 부분이 스프링 부트가 생성하는 스프링 컨테이너이다.
		- 이름 그대로 애노테이션 기반 설정이 가능하고, 서블릿 웹 서버를 지원하는 스프링 컨테이너이다.

	스프링 부트 내부에서 내장 톰캣을 생성하는 코드
		org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory 클래스 getWebServer() 메소드 확인
		- Tomcat tomcat = new Tomcat() 으로 내장 톰캣을 생성
	*/

	public static void main(String[] args) {
		/*
		1. 스프링 컨테이너를 생성한다.
		2. WAS(내장 톰캣)를 생성한다.
		*/
		SpringApplication.run(BootApplication.class, args);
	}
}
