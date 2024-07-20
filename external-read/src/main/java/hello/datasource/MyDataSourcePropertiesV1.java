package hello.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@Getter
@Setter
/*
@ConfigurationProperties
    스프링은 외부 설정의 묶음 정보를 객체로 변환하는 기능을 제공한다. 이것을 타입 안전한 설정 속성이라 한다.
    객체를 사용하면 타입을 사용할 수 있다.
    따라서 실수로 잘못된 타입이 들어오는 문제도 방지할 수 있고 객체를 통해서 활용할 수 있는 부분들이 많아진다.
    쉽게 이야기해서 외부 설정을 자바 코드로 관리할 수 있는 것이다. 그리고 설정 정보 그 자체도 타입을 가지게 된다.

@ConfigurationProperties 이 있으면 외부 설정을 주입 받는 객체라는 뜻이다.
여기에 외부 설정 KEY 의 묶음 시작점인 my.datasource 를 적어준다.
기본 주입 방식은 자바빈 프로퍼티 방식이다. Getter, Setter 가 필요하다.
*/
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {
    private String url;
    private String username;
    private String password;
    private Etc etc;

    @Getter
    @Setter
    public static class Etc {
        private int maxConnection;  //스프링은 캐밥 표기법을 자바 낙타 표기법으로 중간에서 자동으로 변환
        private Duration timeout;
        private List<String> options;
    }
}
