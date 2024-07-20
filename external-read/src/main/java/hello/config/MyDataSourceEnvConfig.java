package hello.config;

import hello.datasource.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
public class MyDataSourceEnvConfig {
    /*
    설정 데이터(application_backup.properties)를 사용하다가 커맨드 라인 옵션 인수나 자바 시스템 속성으로 변경해도 애플리케이션 코드를 그대로 유지할 수 있다.

    단점
         방식의 단점은 Environment 를 직접 주입받고 env.getProperty(key) 를 통해서 값을 꺼내는 과정을 반복해야 한다는 점이다.
         스프링은 @Value 를 통해서 외부 설정값을 주입 받는 더욱 편리한 기능을 제공한다.
    */

    private final Environment env;

    public MyDataSourceEnvConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public MyDataSource myDataSource() {
        //Environment.getProperty(key, Type) 를 호출할 때 타입 정보를 주면 해당 타입으로 변환해준다. (스프링 내부 변환기가 작동한다.)
        //https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.conversion
        return MyDataSource.builder()
                           .url(env.getProperty("my.datasource.url"))
                           .username(env.getProperty("my.datasource.username"))
                           .password(env.getProperty("my.datasource.password"))
                           .maxConnection(env.getProperty("my.datasource.etc.max-connection", Integer.class))   //문자 -> 숫자
                           .timeout(env.getProperty("my.datasource.etc.timeout", Duration.class))               //문자 -> 기간
                           .options(env.getProperty("my.datasource.etc.options", List.class))                   //문자 -> List
                           .build();
    }
}
