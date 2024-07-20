package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDataSourcePropertiesV2.class)
public class MyDataSourceConfigV2 {
    /*
    application_backup.properties 에 필요한 외부 설정을 추가하고 @ConfigurationProperties 의 생성자 주입을 통해서 값을 읽어들였다.
    Setter 가 없으므로 개발자가 중간에 실수로 값을 변경하는 문제가 발생하지 않는다.

    문제
        타입과 객체를 통해서 숫자에 문자가 들어오는 것 같은 기본적인 타입 문제들은 해결이 되었다.
        그런데 타입은 맞는데 숫자의 범위가 기대하는 것과 다르면 어떻게 될까?
        예를 들어서 max-connection 의 값을 0 으로 설정하면 커넥션이 하나도 만들어지지 않는 심각한 문제가 발생한다고 가정해보자.
        max-connection 은 최소 1 이상으로 설정하지 않으면 애플리케이션 로딩 시점에 예외를 발생시켜서 빠르게 문제를 인지할 수 있도록 하고 싶다.
    */

    private final MyDataSourcePropertiesV2 properties;

    public MyDataSourceConfigV2(MyDataSourcePropertiesV2 properties) {
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
