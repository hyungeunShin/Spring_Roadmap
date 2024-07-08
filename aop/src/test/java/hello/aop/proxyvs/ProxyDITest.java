package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ProxyDIAspect.class)
@SpringBootTest(properties = "spring.aop.proxy-target-class=false") //JDK
//@SpringBootTest(properties = "spring.aop.proxy-target-class=true") //CGLIB
class ProxyDITest {
    /*
    JDK 동적 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계를 주입할 수 없다.
    CGLIB 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계 주입을 할 수 있다.

    실제로 개발할 때는 인터페이스가 있으면 인터페이스를 기반으로 의존관계 주입을 받는 것이 맞다.
    DI의 장점이 무엇인가? DI 받는 클라이언트 코드의 변경 없이 구현 클래스를 변경할 수 있는 것이다.
    이렇게 하려면 인터페이스를 기반으로 의존관계를 주입 받아야 한다.
    구현 클래스에 의존관계를 주입하면 향후 구현 클래스를 변경할 때 의존관계 주입을 받는 클라이언트의 코드도 함께 변경해야 한다.
    따라서 올바르게 잘 설계된 애플리케이션이라면 이런 문제가 자주 발생하지는 않는다.
    그럼에도 불구하고 테스트, 또는 여러가지 이유로 AOP 프록시가 적용된 구체 클래스를 직접 의존관계 주입 받아야 하는 경우가 있을 수 있다.
    이때는 CGLIB 를 통해 구체 클래스 기반으로 AOP 프록시를 적용하면 된다.

    그렇다면 인터페이스와 구현클래스 모두 적용 가능한 CGLIB 를 사용하는 것이 좋아보인다.

    ※ CGLIB 단점
    CGLIB 는 구체 클래스를 상속 받기 때문에 다음과 같은 문제가 있다.
        - 대상 클래스에 기본 생성자 필수
            CGLIB 는 구체 클래스를 상속 받는다.
            자바 언어에서 상속을 받으면 자식 클래스의 생성자를 호출할 때 자식 클래스의 생성자에서 부모 클래스의 생성자도 호출해야 한다.
            이 부분이 생략되어 있다면 자식 클래스의 생성자 첫줄에 부모 클래스의 기본 생성자를 호출하는 super() 가 자동으로 들어간다.
            이 부분은 자바 문법 규약이다.

            CGLIB 를 사용할 때 CGLIB 가 만드는 프록시의 생성자는 우리가 호출하는 것이 아니다.
            CGLIB 프록시는 대상 클래스를 상속 받고, 생성자에서 대상 클래스의 기본 생성자를 호출한다. 따라서 대상 클래스에 기본 생성자를 만들어야 한다.
            (기본 생성자는 파라미터가 하나도 없는 생성자를 뜻한다. 생성자가 하나도 없으면 자동으로 만들어진다.)

        - 생성자 2번 호출 문제
            CGLIB 는 구체 클래스를 상속 받는다. 자바 언어에서 상속을 받으면 자식 클래스의 생성자를 호출할 때 부모 클래스의 생성자도 호출해야 한다.
            그런데 왜 2번일까?
                1. 실제 target 의 객체를 생성할 때
                2. 프록시 객체를 생성할 때 부모 클래스의 생성자 호출

        - final 키워드 클래스, 메서드 사용 불가
            final 키워드가 클래스에 있으면 상속이 불가능하고, 메서드에 있으면 오버라이딩이 불가능하다.
            CGLIB 는 상속을 기반으로 하기 때문에 두 경우 프록시가 생성되지 않거나 정상 동작하지 않는다.
            프레임워크 같은 개발이 아니라 일반적인 웹 애플리케이션을 개발할 때는 final 키워드를 잘 사용하지 않는다.
            따라서 이 부분이 특별히 문제가 되지는 않는다.

    ※ 스프링의 해결책
        - 스프링 3.2부터 CGLIB 를 스프링 내부에 함께 패키징
             CGLIB 를 사용하려면 CGLIB 라이브러리가 별도로 필요했다.
             스프링은 CGLIB 라이브러리를 스프링 내부에 함께 패키징해서 별도의 라이브러리 추가 없이 CGLIB 를 사용할 수 있게 되었다.
             org.springframework:spring-core.cglib

        - CGLIB 기본 생성자 필수 문제 해결
            스프링 4.0부터 CGLIB 의 기본 생성자가 필수인 문제가 해결되었다.
            objenesis 라는 특별한 라이브러리를 사용해서 기본 생성자 없이 객체 생성이 가능하다.
            참고로 이 라이브러리는 생성자 호출 없이 객체를 생성할 수 있게 해준다.

        - 생성자 2번 호출 문제
            스프링 4.0부터 CGLIB 의 생성자 2번 호출 문제가 해결되었다.
            이것도 역시 objenesis 라는 특별한 라이브러리 덕분에 가능해졌다.
            이제 생성자가 1번만 호출된다.

        - 스프링 부트 2.0 - CGLIB 기본 사용
            스프링 부트 2.0 버전부터 CGLIB 를 기본으로 사용하도록 했다.
            이렇게 해서 구체 클래스 타입으로 의존관계를 주입하는 문제를 해결했다.
            스프링 부트는 별도의 설정이 없다면 AOP 를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다.
            따라서 인터페이스가 있어도 JDK 동적 프록시를 사용하는 것이 아니라 항상 CGLIB 를 사용해서 구체클래스를 기반으로 프록시를 생성한다.
            물론 스프링은 우리에게 선택권을 열어주기 때문에 설정을 통해 JDK 동적 프록시도 사용할 수 있다.
    */

    @Autowired
    MemberService memberService;    //JDK, CGLIB 둘 다 성공

    @Autowired
    MemberServiceImpl memberServiceImpl;    //JDK 실패, CGLIB 성공

    /*
    SpringBootTest(properties = "spring.aop.proxy-target-class=false") 일 때 에러가 발생한다.
        Bean named 'memberServiceImpl' is expected to be of type 'hello.aop.member.MemberServiceImpl' but was actually of type 'jdk.proxy3.$Proxy55'

        @Autowired MemberService memberService;
            JDK Proxy 는 MemberService 인터페이스를 기반으로 만들어진다.
            따라서 해당 타입으로 캐스팅 할 수 있다.

        @Autowired MemberServiceImpl memberServiceImpl
            JDK Proxy 는 MemberService 인터페이스를 기반으로 만들어진다.
            따라서 MemberServiceImpl 타입이 뭔지 전혀 모른다. 그래서 해당 타입에 주입할 수 없다.
    */
    @Test
    void go() {
        log.info("memberService class : {}", memberService.getClass());
        log.info("memberServiceImpl class : {}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
