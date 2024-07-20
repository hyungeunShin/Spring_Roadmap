package hello.datasource;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;
import java.util.List;

@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {
    /*
    @DefaultValue : 해당 값을 찾을 수 없는 경우 기본값을 사용한다.
        - @DefaultValue Etc etc
            etc 를 찾을 수 없을 경우 Etc 객체를 생성하고 내부에 들어가는 값은 비워둔다. (null , 0)
        - @DefaultValue("DEFAULT") List<String> options
            options 를 찾을 수 없을 경우 DEFAULT 라는 이름의 값을 사용한다.

    참고
        스프링 부트 3.0 이전에는 생성자 바인딩 시에 @ConstructorBinding 애노테이션을 필수로 사용해야 했다.
        스프링 부트 3.0 부터는 생성자가 하나일 때는 생략할 수 있다.
        생성자가 둘 이상인 경우에는 사용할 생성자에 @ConstructorBinding 애노테이션을 적용하면 된다.
    */

    private String url;
    private String username;
    private String password;
    private Etc etc;

    public MyDataSourcePropertiesV2(String url, String username, String password, @DefaultValue Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {
        private int maxConnection;
        private Duration timeout;
        private List<String> options;

        public Etc(int maxConnection, Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
            this.maxConnection = maxConnection;
            this.timeout = timeout;
            this.options = options;
        }
    }
}
