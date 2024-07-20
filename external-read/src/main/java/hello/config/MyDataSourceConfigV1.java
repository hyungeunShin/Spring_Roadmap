package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
//스프링에게 사용할 @ConfigurationProperties 를 지정해주어야 한다.
//이렇게 하면 해당 클래스는 스프링 빈으로 등록되고 필요한 곳에서 주입 받아서 사용할 수 있다.
@EnableConfigurationProperties(MyDataSourcePropertiesV1.class)
public class MyDataSourceConfigV1 {
    /*
    @ConfigurationPropertiesScan
        - @ConfigurationProperties 를 하나하나 직접 등록할 때는 @EnableConfigurationProperties 를 사용한다.
            @EnableConfigurationProperties(MyDataSourcePropertiesV1.class)
        - @ConfigurationProperties 를 특정 범위로 자동 등록할 때는 @ConfigurationPropertiesScan 을 사용하면 된다.
            @SpringBootApplication
            @ConfigurationPropertiesScan({"com.example.app", "com.example.another"})
            public class MyApplication {}

    문제
        MyDataSourcePropertiesV1 은 스프링 빈으로 등록된다.
        그런데 Setter 를 가지고 있기 때문에 누군가 실수로 값을 변경하는 문제가 발생할 수 있다.
        여기에 있는 값들은 외부 설정값을 사용해서 초기에만 설정되고 이후에는 변경하면 안된다.
        이럴 때 Setter 를 제거하고 대신에 생성자를 사용하면 중간에 데이터를 변경하는 실수를 근본적으로 방지할 수 있다.
    */

    //설정 속성을 생성자를 통해 주입 받아서 사용한다.
    private final MyDataSourcePropertiesV1 properties;

    public MyDataSourceConfigV1(MyDataSourcePropertiesV1 properties) {
        this.properties = properties;
    }

    @Bean
    public MyDataSource myDataSource() {
        return MyDataSource.builder()
                           .url(properties.getUrl())
                           .username(properties.getUsername())
                           .password(properties.getPassword())
                           .maxConnection(properties.getEtc().getMaxConnection())
                           .timeout(properties.getEtc().getTimeout())
                           .options(properties.getEtc().getOptions())
                           .build();
    }
}
