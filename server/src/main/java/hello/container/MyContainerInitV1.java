package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;

import java.util.Set;

public class MyContainerInitV1 implements ServletContainerInitializer {
    /*
    서블릿 컨테이너 초기화1
        - WAS 를 실행하는 시점에 필요한 초기화 작업들이 있다.
          서비스에 필요한 필터와 서블릿을 등록하고, 여기에 스프링을 사용한다면 스프링 컨테이너를 만들고, 서블릿과 스프링을 연결하는 디스페처 서블릿도 등록해야 한다.
        - WAS 가 제공하는 초기화 기능을 사용하면, WAS 실행 시점에 이러한 초기화 과정을 진행할 수 있다.
        - 과거에는 web.xml 을 사용해서 초기화했지만, 지금은 서블릿 스펙에서 자바 코드를 사용한 초기화도 지원한다.

    서블릿 컨테이너 초기화 방법
        ServletContainerInitializer 를 구현한 클래스 생성한 후에 WAS 에게 실행할 초기화 클래스 알려줘야 한다.
        -> resources/META-INF/services/jakarta.servlet.ServletContainerInitializer 생성
        -> jakarta.servlet.ServletContainerInitializer 안에 hello.container.MyContainerInitV1 작성
    */

    /*
    Set<Class<?>> set : 조금 더 유연한 초기화를 기능을 제공한다. @HandlesTypes 애노테이션과 함께 사용한다.
    ServletContext servletContext : 서블릿 컨테이너 자체의 기능을 제공한다. 이 객체를 통해 필터나 서블릿을 등록할 수 있다.
    */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) {
        System.out.println("MyContainerInitV1.onStartup");
        System.out.println("MyContainerInitV1 classSet = " + set);
        System.out.println("MyContainerInitV1 servletContext = " + servletContext);
    }
}
