package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PackageLogTraceProxyPostProcessor implements BeanPostProcessor {
    /*
    빈 후처리기는 빈을 조작하고 변경할 수 있는 후킹 포인트이다.
    이것은 빈 객체를 조작하거나 심지어 다른 객체로 바꾸어 버릴 수 있을 정도로 막강하다.
    여기서 조작이라는 것은 해당 객체의 특정 메서드를 호출하는 것을 뜻한다.
    일반적으로 스프링 컨테이너가 등록하는 특히 컴포넌트 스캔의 대상이 되는 빈들은 중간에 조작할 방법이 없는데, 빈 후처리기를 사용하면 개발자가 등록하는 모든 빈을 중간에 조작할 수 있다.
    이 말은 빈 객체를 프록시로 교체하는 것도 가능하다는 뜻이다.

    참고 - @PostConstruct 의 비밀
    @PostConstruct 는 스프링 빈 생성 이후에 빈을 초기화 하는 역할을 한다.
    그런데 생각해보면 빈의 초기화 라는 것이 단순히 @PostConstruct 애노테이션이 붙은 초기화 메서드를 한번 호출만 하면 된다.
    쉽게 이야기해서 생성된 빈을 한번 조작하는 것이다.
    따라서 빈을 조작하는 행위를 하는 적절한 빈 후처리기가 있으면 될 것 같다.
    스프링은 CommonAnnotationBeanPostProcessor 라는 빈 후처리기를 자동으로 등록하는데 여기에서 @PostConstruct 애노테이션이 붙은 메서드를 호출한다.
    따라서 스프링 스스로도 스프링 내부의 기능을 확장하기 위해 빈 후처리기를 사용한다.
    */

    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTraceProxyPostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("beanName : {}, bean : {}", beanName, bean.getClass());

        //프록시 적용 대상 여부 체크
        //프록시 적용 대상이 아니면 원본 그대로 반환
        String packageName = bean.getClass().getPackageName();
        if(!packageName.startsWith(basePackage)) {
            return bean;
        }

        /*
        모든 스프링 빈들에 프록시를 적용할 필요는 없다.
        여기서는 특정 패키지와 그 하위에 위치한 스프링 빈들만 프록시를 적용한다.
        hello.proxy.app 와 관련된 부분에만 적용하고 다른 패키지의 객체들은 원본 객체를 그대로 반환한다.
        */
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        //프록시 팩토리는 advisor 가 필요하기 때문에 이 부분은 외부에서 주입 받도록 했다.
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy --> target : {}, proxy : {}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
