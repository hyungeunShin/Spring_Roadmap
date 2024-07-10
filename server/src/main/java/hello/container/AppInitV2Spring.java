package hello.container;

import hello.spring.HelloConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitV2Spring implements AppInit {
    /*
    스프링 컨테이너 등록
        다음과 같은 과정이 필요하다.
            - 스프링 컨테이너 만들기
            - 스프링 MVC 컨트롤러를 스프링 컨테이너에 빈으로 등록하기
            - 스프링 MVC 를 사용하는데 필요한 디스패처 서블릿을 서블릿 컨테이너 등록하기
    */

    /*
    AppInitV2Spring 는 AppInit 을 구현했다.
    AppInit 을 구현하면 애플리케이션 초기화 코드가 자동으로 실행된다. 앞서 MyContainerInitV2 에 관련 작업을 이미 해두었다.

    스프링 컨테이너 생성
        - AnnotationConfigWebApplicationContext 가 바로 스프링 컨테이너이다.
            - AnnotationConfigWebApplicationContext 부모를 따라가 보면 ApplicationContext 인터페이스를 확인할 수 있다.
            - 이 구현체는 이름 그대로 애노테이션 기반 설정과 웹 기능을 지원하는 스프링 컨테이너로 이해하면 된다.
        - appContext.register(HelloConfig.class)
            - 컨테이너에 스프링 설정을 추가한다.

    스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        - new DispatcherServlet(appContext)
        - 코드를 보면 스프링 MVC 가 제공하는 디스패처 서블릿을 생성하고, 생성자에 앞서 만든 스프링 컨테이너를 전달하는 것을 확인할 수 있다.
          이렇게 하면 디스패처 서블릿에 스프링 컨테이너가 연결된다.
        - 이 디스패처 서블릿에 HTTP 요청이 오면 디스패처 서블릿은 해당 스프링 컨테이너에 들어있는 컨트롤러 빈들을 호출한다.

    디스패처 서블릿을 서블릿 컨테이너에 등록
        - servletContext.addServlet("dispatcherV2", dispatcher)
            - 디스패처 서블릿을 서블릿 컨테이너에 등록한다.
        - /spring/* 요청이 디스패처 서블릿을 통하도록 설정
            - /spring/* 이렇게 경로를 지정하면 /spring 과 그 하위 요청은 모두 해당 서블릿을 통하게 된다.
    */
    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("AppInitV2Spring.onStartup");

        //스프링 컨테이너 생성
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(HelloConfig.class);

        //스프링 MVC 디스패처 서블릿 생성, 스프링 컨테이너 연결
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        //디스패처 서블릿을 서블릿 컨테이너에 등록
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherV2", dispatcherServlet);
        servlet.addMapping("/spring/*");    //http://localhost/spring/hello-spring
    }
}
