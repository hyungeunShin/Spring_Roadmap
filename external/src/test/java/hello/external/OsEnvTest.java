package hello.external;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Slf4j
class OsEnvTest {
    /*
    OS 환경 변수를 설정하고, 필요한 곳에서 System.getenv() 를 사용하면 외부 설정을 사용할 수 있다.
    이제 데이터베이스 접근 URL 과 같은 정보를 OS 환경 변수에 설정해두고 읽어들이면 된다.
    예를 들어서 개발 서버에서는 DB_URL=dev.db.com 과 같이 설정하고, 운영 서버에서는 DB_URL=prod.db.com 와 같이 설정하는 것이다.
    이렇게 하면 System.getenv("DB_URL") 을 조회할 때 각각 환경에 따라서 서로 다른 값을 읽게 된다.

    하지만 OS 환경 변수는 이 프로그램 뿐만 아니라 다른 프로그램에서도 사용할 수 있다.
    쉽게 이야기해서 전역 변수 같은 효과가 있다.
    여러 프로그램에서 사용하는 것이 맞을 때도 있지만, 해당 애플리케이션을 사용하는 자바 프로그램 안에서만 사용되는 외부 설정값을 사용하고 싶을 때도 있다.
    */
    @Test
    void test() {
        Map<String, String> map = System.getenv();
        map.forEach((key, value) -> log.info("{} : {}", key, value));
    }
}
