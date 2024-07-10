package hello.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration  //@MySpringBootApplication 에 ComponentScan 활용
public class HelloConfig {
    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
