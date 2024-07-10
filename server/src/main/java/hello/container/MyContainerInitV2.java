package hello.container;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;

import java.util.Set;

@HandlesTypes(AppInit.class)
public class MyContainerInitV2 implements ServletContainerInitializer {
    /*
    서블릿을 등록하는 2가지 방법
        - @WebServlet 애노테이션
        - 프로그래밍 방식

        참고 - 프로그래밍 방식을 사용하는 이유
            @WebServlet 을 사용하면 애노테이션 하나로 서블릿을 편리하게 등록할 수 있다.
            하지만 애노테이션 방식을 사용하면 유연하게 변경하는 것이 어렵다. 마치 하드코딩 된 것 처럼 동작한다.
            TestServlet.class 를 보면 /test 경로를 변경하고 싶으면 코드를 직접 변경해야 바꿀 수 있다.
            반면에 프로그래밍 방식은 코딩을 더 많이 해야하고 불편하지만 무한한 유연성을 제공한다.

            예를 들어서
                - /hello-servlet 경로를 상황에 따라서 바꾸어 외부 설정을 읽어서 등록할 수 있다.
                - 서블릿 자체도 특정 조건에 따라서 if 문으로 분기해서 등록하거나 뺄 수 있다.
                - 서블릿을 내가 직접 생성하기 때문에 생성자에 필요한 정보를 넘길 수 있다.

    애플리케이션 초기화
        서블릿 컨테이너는 조금 더 유연한 초기화 기능을 지원한다.
        여기서는 이것을 애플리케이션 초기화라 하겠다.
        애플리케이션 초기화를 진행하려면 먼저 인터페이스를 만들어야 한다.
        내용과 형식은 상관없고, 인터페이스는 꼭 필요하다.
        AppInit 인터페이스를 만들었다.

        초기화 과정
        1. @HandlesTypes 애노테이션에 애플리케이션 초기화 인터페이스를 지정한다.
            - 여기서는 앞서 만든 AppInit.class 인터페이스를 지정했다.
        2. 서블릿 컨테이너 초기화(ServletContainerInitializer)는 파라미터로 넘어오는 Set<Class<?>> set 에 애플리케이션 초기화 인터페이스의 구현체들을 모두 찾아서 클래스 정보로 전달한다.
            - 여기서는 @HandlesTypes(AppInit.class) 를 지정했으므로 AppInit.class 의 구현체인 AppInitV1Servlet.class 정보가 전달된다.
            - 참고로 객체 인스턴스가 아니라 클래스 정보를 전달하기 때문에 실행하려면 객체를 생성해서 사용해야 한다.
        3. appInitClass.getDeclaredConstructor().newInstance()
            - 리플렉션을 사용해서 객체를 생성한다. 참고로 이 코드는 new AppInitV1Servlet() 과 같다 생각하면 된다.
        4. appInit.onStartup(servletContext)
            - 애플리케이션 초기화 코드를 직접 실행하면서 서블릿 컨테이너 정보가 담긴 servletContext 도 함께 전달한다.

        참고
            서블릿 컨테이너 초기화만 있어도 될 것 같은데, 왜 이렇게 복잡하게 애플리케이션 초기화라는 개념을 만들었을까?

            편리함
                - 서블릿 컨테이너를 초기화 하려면 ServletContainerInitializer 인터페이스를 구현한 코드를 만들어야 한다.
                  여기에 추가로 META-INF/services/jakarta.servlet.ServletContainerInitializer 파일에 해당 코드를 직접 지정해주어야 한다.
                - 애플리케이션 초기화는 특정 인터페이스만 구현하면 된다.
            의존성
                - 애플리케이션 초기화는 서블릿 컨테이너에 상관없이 원하는 모양으로 인터페이스를 만들 수 있다.
                  이를 통해 애플리케이션 초기화 코드가 서블릿 컨테이너에 대한 의존을 줄일 수 있다.
                  특히 ServletContext servletContext 가 필요없는 애플리케이션 초기화 코드라면 의존을 완전히 제거할 수도 있다
    */

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyContainerInitV2.onStartup");
        System.out.println("MyContainerInitV2 classSet = " + set);
        System.out.println("MyContainerInitV2 servletContext = " + servletContext);

        //class hello.container.AppInitV1Servlet
        set.forEach(c -> {
            try {
                AppInit appInit = (AppInit) c.getDeclaredConstructor().newInstance();   //new AppInitV1Servlet()과 같은 코드
                appInit.onStartup(servletContext);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
