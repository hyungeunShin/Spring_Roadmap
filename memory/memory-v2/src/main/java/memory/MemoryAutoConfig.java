package memory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryAutoConfig {
    /*
    @AutoConfiguration
        스프링 부트가 제공하는 자동 구성 기능을 적용할 때 사용하는 애노테이션이다.
    @ConditionalOnProperty
        memory=on 이라는 환경 정보가 있을 때 라이브러리를 적용한다. (스프링 빈을 등록한다.)
        라이브러리를 가지고 있더라도 상황에 따라서 해당 기능을 켜고 끌 수 있게 유연한 기능을 제공한다.

    자동 구성 대상 지정
        스프링 부트 자동 구성을 적용하려면, 다음 파일에 자동 구성 대상을 꼭 지정해주어야 한다.
        <src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports>
            memory.MemoryAutoConfig
    */

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
