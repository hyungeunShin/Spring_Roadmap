package hello.selector;

import hello.selector.code.HelloBean;
import hello.selector.code.HelloConfig;
import hello.selector.code.HelloImportSelector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

class ImportSelectorTest {
    @Test
    void staticConfig() {
        /*
        스프링 컨테이너를 만들고, StaticConfig.class 를 초기 설정 정보로 사용했다.
        그 결과 HelloBean 이 스프링 컨테이너에 잘 등록된 것을 확인할 수 있다.
        */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StaticConfig.class);
        HelloBean bean = context.getBean(HelloBean.class);
        Assertions.assertThat(bean).isNotNull();
    }

    @Test
    void selectorConfig() {
        /*
        SelectorConfig 는 @Import(HelloImportSelector.class) 에서 ImportSelector 의 구현체인 HelloImportSelector 를 사용했다.
        스프링은 HelloImportSelector 를 실행하고, "hello.selector.HelloConfig" 라는 문자를 반환 받는다.
        스프링은 이 문자에 맞는 대상을 설정 정보로 사용한다.
        따라서 hello.selector.HelloConfig 이 설정 정보로 사용된다.
        그 결과 HelloBean 이 스프링 컨테이너에 잘 등록된 것을 확인할 수 있다.
        */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SelectorConfig.class);
        HelloBean bean = context.getBean(HelloBean.class);
        Assertions.assertThat(bean).isNotNull();
    }

    @Configuration
    @Import(HelloConfig.class)
    public static class StaticConfig {

    }

    @Configuration
    @Import(HelloImportSelector.class)
    public static class SelectorConfig {

    }
}
