package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ProxyFactoryTest {
    @Test
    @DisplayName("인터페이스가 존재하면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        /*
        프록시 팩토리를 생성할 때 생성자에 프록시의 호출 대상을 함께 넘겨준다.
        프록시 팩토리는 이 인스턴스 정보를 기반으로 프록시를 만들어낸다.
        만약 이 인스턴스에 인터페이스가 있다면 JDK 동적 프록시를 기본으로 사용하고 인터페이스가 없고 구체 클래스만 있다면 CGLIB 를 통해서 동적 프록시를 생성한다.
        */
        ProxyFactory proxyFactory = new ProxyFactory(target);
        /*
        프록시 팩토리를 통해서 만든 프록시가 사용할 부가 기능 로직을 설정한다.
        JDK 동적 프록시가 제공하는 InvocationHandler 와 CGLIB 가 제공하는 MethodInterceptor 의 개념과 유사하다.
        이렇게 프록시가 제공하는 부가 기능 로직을 어드바이스(Advice)라 한다.
        */
        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass : {}", target.getClass());    //class hello.proxy.common.service.ServiceImpl
        log.info("proxyClass : {}", proxy.getClass());      //class jdk.proxy3.$Proxy12

        proxy.save();
        proxy.find();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());

        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass : {}", target.getClass());
        log.info("proxyClass : {}", proxy.getClass());

        proxy.call();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        //인터페이스가 있어도 강제로 CGLIB 를 사용
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass : {}", target.getClass());    //class hello.proxy.common.service.ServiceImpl
        log.info("proxyClass : {}", proxy.getClass());      //class hello.proxy.common.service.ServiceImpl$$SpringCGLIB$$0

        proxy.save();
        proxy.find();

        assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
