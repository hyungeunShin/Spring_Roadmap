package hello.config;

import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfig {
    /*
    memory-v1.jar
        스프링 부트 플러그인을 사용하게 실행 가능한 Jar 구조를 기본으로 만든다.
        여기서는 실행 가능한 Jar 가 아니라, 다른곳에 포함되어서 사용할 순수 라이브러리 Jar 를 만드는 것이 목적이므로 스프링 부트 플러그인을 사용하지 않았다.

        - META-INF
            - MANIFEST.MF
        - memory
            - MemoryFinder.class
            - MemoryController.class
            - Memory.class

    project-v1 에서 memory-v1.jar 임포트
        1. project-v1/libs 폴더를 생성
        2. memory-v1 프로젝트에서 빌드한 memory-v1.jar 를 이곳에 복사
        3. project-v1/build.gradle 에 memory-v1.jar 를 추가
            implementation files('libs/memory-v1.jar')

        외부 라이브러리를 직접 만들고 또 그것을 프로젝트에 라이브러리로 불러서 적용하였다.
        그런데 라이브러리를 사용하는 클라이언트 개발자 입장을 생각해보면, 라이브러리 내부에 있는 어떤 빈을 등록해야하는지 알아야 하고, 그것을 또 하나하나 빈으로 등록해야 한다.
        지금처럼 간단한 라이브러리가 아니라 초기 설정이 복잡하다면 사용자 입장에서는 상당히 귀찮은 작업이 될 수 있다.
        이런 부분을 자동으로 처리해주는 것이 바로 스프링 부트 자동 구성(Auto Configuration)이다.
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
