package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV3SpringMvc implements WebApplicationInitializer {
    /*
    스프링 MVC 서블릿 컨테이너 초기화 지원
        지금까지의 과정을 생각해보면 서블릿 컨테이너를 초기화 하기 위해 다음과 같은 복잡한 과정을 진행했다.
        - ServletContainerInitializer 인터페이스를 구현해서 서블릿 컨테이너 초기화 코드를 만들었다.
        - 여기에 애플리케이션 초기화를 만들기 위해 @HandlesTypes 애노테이션을 적용했다.
        - /META-INF/services/jakarta.servlet.ServletContainerInitializer 파일에 서블릿 컨테이너 초기화 클래스 경로를 등록했다.

        서블릿 컨테이너 초기화 과정은 상당히 번거롭고 반복되는 작업이다.
        스프링 MVC 는 이러한 서블릿 컨테이너 초기화 작업을 이미 만들어두었다.
        덕분에 개발자는 서블릿 컨테이너 초기화 과정은 생략하고, 애플리케이션 초기화 코드만 작성하면 된다.

    현재 등록된 서블릿 다음과 같다.
        - / = dispatcherV3
        - /spring/* = dispatcherV2
        - /hello-servlet = helloServlet
        - /test = TestServlet
        - 이런 경우 우선순위는 더 구체적인 것이 먼저 실행된다

     참고
        여기서는 이해를 돕기 위해 디스패처 서블릿도 2개 만들고, 스프링 컨테이너도 2개 만들었다.
        일반적으로는 스프링 컨테이너를 하나 만들고, 디스패처 서블릿도 하나만 만든다.
        그리고 디스패처 서블릿 의 경로 매핑도 / 로 해서 하나의 디스패처 서블릿을 통해서 모든 것을 처리하도록 한다.

     spring-web 라이브러리를 열어보면 서블릿 컨테이너 초기화를 위한 등록 파일을 확인할 수 있다. -> /META-INF/services/jakarta.servlet.ServletContainerInitializer
     그리고 이곳에 서블릿 컨테이너 초기화 클래스가 등록되어 있다.
     스프링 MVC 도 우리가 지금까지 한 것 처럼 서블릿 컨테이너 초기화 파일에 초기화 클래스를 등록해두었다.
     그리고 WebApplicationInitializer 인터페이스를 애플리케이션 초기화 인터페이스로 지정해두고, 이것을 생성해서 실행한다.
     따라서 스프링 MVC 를 사용한다면 WebApplicationInitializer 인터페이스만 구현하면
     AppInitV3SpringMvc 에서 본 것 처럼 편리하게 애플리케이션 초기화를 사용할 수 있다.
     */

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("AppInitV3SpringMvc.onStartup");

        //스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(HelloConfig.class);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        //디스패처 서블릿을 서블릿 컨테이너에 등록
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV3", dispatcherServlet);

        //모든 요청이 디스패처 서블릿을 통하도록 설정
        servlet.addMapping("/");
    }
}
