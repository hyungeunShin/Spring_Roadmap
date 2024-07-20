package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDataSourcePropertiesV3.class)
public class MyDataSourceConfigV3 {
    /*
    ConfigurationProperties 덕분에 타입 안전하고 또 매우 편리하게 외부 설정을 사용할 수 있다.
    그리고 검증기 덕분에 쉽고 편리하게 설정 정보를 검증할 수 있다.
    가장 좋은 예외는 컴파일 예외 그리고 애플리케이션 로딩 시점에 발생하는 예외이다.
    가장 나쁜 예외는 고객 서비스 중에 발생하는 런타임 예외이다.

    ConfigurationProperties 장점
        - 외부 설정을 객체로 편리하게 변환해서 사용할 수 있다.
        - 외부 설정의 계층을 객체로 편리하게 표현할 수 있다.
        - 외부 설정을 타입 안전하게 사용할 수 있다.
        - 검증기를 적용할 수 있다.
    */

    private final MyDataSourcePropertiesV3 properties;

    public MyDataSourceConfigV3(MyDataSourcePropertiesV3 properties) {
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
