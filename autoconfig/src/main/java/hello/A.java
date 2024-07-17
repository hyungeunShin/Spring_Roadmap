package hello;

public class A {
    /*
    스프링 부트의 자동 구성
        - 스프링 부트는 자동 구성(Auto Configuration)이라는 기능을 제공하는데, 일반적으로 자주 사용하는 수 많은 빈들을 자동으로 등록해주는 기능이다.
        - 스프링 부트는 org.springframework.boot:spring-boot-autoconfigure 라는 라이브러리 안에서 수 많은 자동 구성을 제공한다.
          스프링 부트 프로젝트를 사용하면 spring-boot-autoconfigure 라이브러리는 기본적으로 사용된다.
        - 스프링 부트가 제공하는 자동 구성(AutoConfiguration)
            https://docs.spring.io/spring-boot/appendix/auto-configuration-classes/core.html

    Auto Configuration - 용어, 자동 설정? 자동 구성?
        Auto Configuration 은 주로 다음 두 용어로 번역되어 사용된다.
            - 자동 설정
                Configuration 이라는 단어가 컴퓨터 용어에서는 환경 설정, 설정이라는 뜻으로 자주 사용된다.
                Auto Configuration 은 크게 보면 빈들을 자동으로 등록해서 스프링이 동작하는 환경을 자동으로 설정해주기 때문에 자동 설정이라는 용어도 맞다.
            - 자동 구성
                Configuration 이라는 단어는 구성, 배치라는 뜻도 있다.
                예를 들어서 컴퓨터라고 하면 CPU, 메모리등을 배치해야 컴퓨터가 동작한다. 이렇게 배치하는 것을 구성이라 한다.
                스프링도 스프링 실행에 필요한 빈들을 적절하게 배치해야 한다.
                자동 구성은 스프링 실행에 필요한 빈들을 자동으로 배치해주는 것이다.

            자동 설정, 자동 구성 두 용어 모두 맞는 말이다.
            자동 설정은 넓게 사용되는 의미이고, 자동 구성은 실행에 필요한 컴포넌트 조각을 자동으로 배치한다는 더 좁은 의미에 가깝다.
                - Auto Configuration 은 자동 구성이라는 단어를 주로 사용하고, 문맥에 따라서 자동 설정이라는 단어도 사용하겠다.
                - Configuration 이 단독으로 사용될 때는 설정이라는 단어를 사용하겠다.

        정리
            - @Conditional : 특정 조건에 맞을 때 설정이 동작하도록 한다.
            - @AutoConfiguration : 자동 구성이 어떻게 동작하는지 내부 원리 이해

    @Conditional
        - memory 패키지 기능을 항상 사용하는 것이 아니라 특정 조건일 때만 해당 기능이 활성화 되도록 해보자.
        - 예를 들어서 개발 서버에서 확인 용도로만 해당 기능을 사용하고, 운영 서버에서는 해당 기능을 사용하지 않는 것이다.
        - 여기서 핵심은 소스코드를 고치지 않고 이런 것이 가능해야 한다는 점이다.
            - 프로젝트를 빌드해서 나온 빌드 파일을 개발 서버에도 배포하고, 같은 파일을 운영서버에도 배포해야 한다.
        - 같은 소스 코드인데 특정 상황일 때만 특정 빈들을 등록해서 사용하도록 도와주는 기능이 바로 @Conditional 이다.
        - 참고로 이 기능은 스프링 부트 자동 구성에서 자주 사용한다.

        이름 그대로 특정 조건을 만족하는가 하지 않는가를 구별하는 기능이다.
        이 기능을 사용하려면 먼저 Condition 인터페이스를 구현해야 한다.
        <org.springframework.context.annotation - Condition>
            public interface Condition {
                boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);
            }
            - matches() 메서드가 true 를 반환하면 조건에 만족해서 동작하고, false 를 반환하면 동작하지 않는다.
            - ConditionContext : 스프링 컨테이너, 환경 정보등을 담고 있다.
            - AnnotatedTypeMetadata : 애노테이션 메타 정보를 담고 있다.

        Condition 인터페이스를 구현해서 다음과 같이 자바 시스템 속성이 memory=on 이라고 되어 있을 때만 메모리 기능이 동작하도록 만들어보자.
            방법1
                1. MemoryCondition.class 작성
                2. MemoryConfig.class 에 @Conditional(MemoryCondition.class) 작성
                3. Run/Debug Configurations 에서 Modify options -> add VM options -> -Dmemory=on 작성
            방법2
                1. MemoryConfig.class 에 @ConditionalOnProperty(name = "memory", havingValue = "on") 작성
                2. Run/Debug Configurations 에서 Modify options -> add VM options -> -Dmemory=on 작성

        참고
            스프링은 외부 설정을 추상화해서 Environment 로 통합했다.
            그래서 다음과 같은 다양한 외부 환경 설정을 Environment 하나로 읽어들일 수 있다.
            #VM Options
            #java -Dmemory=on -jar project.jar
            -Dmemory=on

            #Program arguments
            # -- 가 있으면 스프링이 환경 정보로 사용
            #java -jar project.jar --memory=on
            --memory=on

            #application.properties
            #application.properties 에 있으면 환경 정보로 사용
            memory=on

        @ConditionalOnXxx
            스프링은 @Conditional 과 관련해서 개발자가 편리하게 사용할 수 있도록 수 많은 @ConditionalOnXxx 를 제공한다.

            - @ConditionalOnClass, @ConditionalOnMissingClass
                클래스가 있는 경우 동작한다. Missing 은 반대
            - @ConditionalOnBean, @ConditionalOnMissingBean
                빈이 등록되어 있는 경우 동작한다. Missing 은 반대
            - @ConditionalOnProperty
                환경 정보가 있는 경우 동작한다.
            - @ConditionalOnResource
                리소스가 있는 경우 동작한다.
            - @ConditionalOnWebApplication, @ConditionalOnNotWebApplication
                웹 애플리케이션인 경우 동작한다.
            - @ConditionalOnExpression
                SpEL 표현식에 만족하는 경우 동작한다.
            참고
                https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html#features.developing-auto-configuration.condition-annotations

            참고
                @Conditional 자체는 스프링 부트가 아니라 스프링 프레임워크의 기능이다.
                스프링 부트는 이 기능을 확장해서 @ConditionalOnXxx 를 제공한다

    @AutoConfiguration
        자동 구성 이해1 - 스프링 부트의 동작
            스프링 부트는 다음 경로에 있는 파일을 읽어서 스프링 부트 자동 구성으로 사용한다.
                resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

            스프링 부트 자동 구성이 동작하는 원리는 다음 순서로 확인할 수 있다.
                @SpringBootApplication -> @EnableAutoConfiguration -> @Import(AutoConfigurationImportSelector.class)

                AutoConfigApplication.class 에서 SpringApplication.run(AutoConfigApplication.class, args); 을 보면
                AutoConfigApplication.class 를 넘겨주는데, 이 클래스를 설정 정보로 사용한다는 뜻이다.
                AutoConfigApplication 에는 @SpringBootApplication 애노테이션이 있는데, 여기에 중요한 설정 정보들이 들어있다.

                @SpringBootApplication 안에 우리가 주목할 애노테이션은 @EnableAutoConfiguration 이다.
                이름 그대로 자동 구성을 활성화 하는 기능을 제공한다.
                @EnableAutoConfiguration 안에 주목할 애노테이션은 @Import({AutoConfigurationImportSelector.class}) 이다.

                AutoConfigurationImportSelector 는 ImportSelector 의 구현체이다.
                따라서 설정 정보를 동적으로 선택할 수 있다.
                실제로 이 코드는 모든 라이브러리에 있는 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports 경로의 파일을 확인한다.

        자동 구성 이해2 - ImportSelector
            @Import 에 설정 정보를 추가하는 방법은 2가지가 있다.
            - 정적인 방법: @Import(클래스) 이것은 정적이다. 코드에 대상이 딱 박혀 있다. 설정으로 사용할 대상을 동적으로 변경할 수 없다.
            - 동적인 방법: @Import(ImportSelector) 코드로 프로그래밍해서 설정으로 사용할 대상을 동적으로 선택할 수 있다.

        정리
            스프링 부트의 자동 구성을 직접 만들어서 사용할 때는 다음을 참고
                - @AutoConfiguration 에 자동 구성의 순서를 지정할 수 있다.
                - @AutoConfiguration 도 설정 파일이다. 내부에 @Configuration 이 있는 것을 확인할 수 있다.
                    - 하지만 일반 스프링 설정과 라이프사이클이 다르기 때문에 컴포넌트 스캔의 대상이 되면 안된다.
                    - 파일에 지정해서 사용해야 한다. --> resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
                    - 그래서 스프링 부트가 제공하는 컴포넌트 스캔에서는 @AutoConfiguration 을 제외하는 AutoConfigurationExcludeFilter 필터가 포함되어 있다.
                - 자동 구성이 내부에서 컴포넌트 스캔을 사용하면 안된다. 대신에 자동 구성 내부에서 @Import 는 사용할 수 있다.

            자동 구성을 언제 사용할까?
                AutoConfiguration 은 라이브러리를 만들어서 제공할 때 사용하고, 그 외에는 사용하는 일이 거의 없다.
                왜냐하면 보통 필요한 빈들을 컴포넌트 스캔하거나 직접 등록하기 때문이다.
                하지만 라이브러리를 만들어서 제공할 때는 자동 구성이 유용하다.
                실제로 다양한 외부 라이브러리들이 자동 구성을 함께 제공한다.
    */
}
