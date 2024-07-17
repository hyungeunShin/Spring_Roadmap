package hello.config;

import memory.MemoryCondition;
import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/*
Memory 패키지를 외부 라이브러리 라고 생각하자.
그렇기 때문에 프로젝트에서 외부 기능을 쓰기 위해 설정 파일은 여기에 위치
*/
@Configuration
/*
- MemoryConfig 의 적용 여부는 @Conditional 에 지정한 MemoryCondition 의 조건에 따라 달라진다.
- MemoryCondition 의 matches() 를 실행해보고 그 결과가 true 이면 MemoryConfig 는 정상 동작한다.
  따라서 memoryController , memoryFinder 가 빈으로 등록된다.
- MemoryCondition 의 실행결과가 false 이면 MemoryConfig 는 무효화 된다.
  그래서 memoryController , memoryFinder 빈은 등록되지 않는다.
*/
//@Conditional(MemoryCondition.class)
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryConfig {
    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
