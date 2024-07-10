package hello;

import hello.boot.MySpringApplication;
import hello.boot.MySpringBootApplication;

@MySpringBootApplication
public class MySpringBootMain {
    /*
    - 패키지 위치가 중요하다. hello 에 위치했다.
    - 여기에 위치한 이유는 @MySpringBootApplication 에 컴포넌트 스캔이 추가되어 있다.
      컴포넌트 스캔의 기본 동작은 해당 애노테이션이 붙은 클래스의 현재 패키지 부터 그 하위 패키지를 컴포넌트 스캔의 대상으로 사용하기 때문이다.
      애노테이션이 붙은 hello.MySpringBootMain 클래스의 패키지 위치는 hello 이므로 그 하위의 hello.spring.HelloController 를 컴포넌트 스캔한다.
    - MySpringApplication.run(설정 정보, args) 이렇게 한줄로 실행하면 된다.
    - 이 기능을 사용하는 개발자는 @MySpringBootApplication 애노테이션과 MySpringApplication.run() 메서드만 기억하면 된다.
    - 이렇게 하면 내장 톰캣 실행, 스프링 컨테이너 생성, 디스패처 서블릿, 컴포넌트 스캔까지 모든 기능이 한번에 편리하게 동작한다.
    */

    public static void main(String[] args) {
        System.out.println("MySpringBootMain.main");
        MySpringApplication.run(MySpringBootMain.class, args);
    }
}
