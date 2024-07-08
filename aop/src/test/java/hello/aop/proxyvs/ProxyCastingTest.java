package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
class ProxyCastingTest {
    /*
    JDK 동적 프록시와 CGLIB 를 사용해서 AOP 프록시를 만드는 방법에는 각각 장단점이 있다.
    JDK 동적 프록시는 인터페이스가 필수이고, 인터페이스를 기반으로 프록시를 생성한다.
    CGLIB 는 구체 클래스를 기반으로 프록시를 생성한다.

    물론 인터페이스가 없고 구체 클래스만 있는 경우에는 CGLIB 를 사용해야 한다.
    그런데 인터페이스가 있는 경우에는 JDK 동적 프록시나 CGLIB 둘중에 하나를 선택할 수 있다.

    스프링이 프록시를 만들때 제공하는 ProxyFactory 에 proxyTargetClass 옵션에 따라 둘중 하나를 선택해서 프록시를 만들 수 있다.
        - proxyTargetClass=false JDK 동적 프록시를 사용해서 인터페이스 기반 프록시 생성
        - proxyTargetClass=true CGLIB 를 사용해서 구체 클래스 기반 프록시 생성
        - 참고로 옵션과 무관하게 인터페이스가 없으면 JDK 동적 프록시를 적용할 수 없으므로 CGLIB 를 사용한다.
    */

    /*
    인터페이스 기반으로 프록시를 생성하는 JDK 동적 프록시는 구체 클래스로 타입 캐스팅이 불가능한 한계가 있다.

    여기서는 MemberServiceImpl 타입을 기반으로 JDK 동적 프록시를 생성했다.
    MemberServiceImpl 타입은 MemberService 인터페이스를 구현한다.
    따라서 JDK 동적 프록시는 MemberService 인터페이스를 기반으로 프록시를 생성한다.
    이 프록시를 JDK Proxy 라고 하자. 여기서 memberServiceProxy 가 바로 JDK Proxy 이다.

    그런데 여기에서 JDK Proxy 를 대상 클래스인 MemberServiceImpl 타입으로 캐스팅 하려고 하니 예외가 발생한다.
    왜냐하면 JDK 동적 프록시는 인터페이스를 기반으로 프록시를 생성하기 때문이다.
    JDK Proxy 는 MemberService 인터페이스를 기반으로 생성된 프록시이다.
    따라서 JDK Proxy 는 MemberService 로 캐스팅은 가능하지만 MemberServiceImpl 이 어떤 것인지 전혀 알지 못한다.
    따라서 MemberServiceImpl 타입으로는 캐스팅이 불가능하다.
    캐스팅을 시도하면 ClassCastException.class 예외가 발생한다.
    */
    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);    //JDK 동적 프록시

        //프록시를 인터페이스로 캐스팅
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy class : {}", memberServiceProxy.getClass());

        //JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 예외 발생
        Assertions.assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    /*
    MemberServiceImpl 타입을 기반으로 CGLIB 프록시를 생성했다.
    MemberServiceImpl 타입은 MemberService 인터페이스를 구현했다.
    CGLIB 는 구체 클래스를 기반으로 프록시를 생성한다.
    따라서 CGLIB 는 MemberServiceImpl 구체 클래스를 기반으로 프록시를 생성한다.
    이 프록시를 CGLIB Proxy 라고 하자. 여기서 memberServiceProxy 가 바로 CGLIB Proxy 이다.

    여기에서 CGLIB Proxy 를 대상 클래스인 MemberServiceImpl 타입으로 캐스팅하면 성공한다.
    왜냐하면 CGLIB 는 구체 클래스를 기반으로 프록시를 생성하기 때문이다.
    CGLIB Proxy 는 MemberServiceImpl 구체 클래스를 기반으로 생성된 프록시이다.
    따라서 CGLIB Proxy 는 MemberServiceImpl 은 물론이고, MemberServiceImpl 이 구현한 인터페이스인 MemberService 로도 캐스팅 할 수 있다.
    */
    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);    //CGLIB 프록시

        //프록시를 인터페이스로 캐스팅
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy class : {}", memberServiceProxy.getClass());

        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
