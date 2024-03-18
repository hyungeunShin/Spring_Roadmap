package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class UncheckedAppTest {
    /*
    런타임 예외 - 대부분 복구 불가능한 예외
        시스템에서 발생한 예외는 대부분 복구 불가능 예외이다.
        런타임 예외를 사용하면 서비스나 컨트롤러가 이런 복구 불가능한 예외를 신경쓰지 않아도 된다.
        물론 이렇게 복구 불가능한 예외는 일관성 있게 공통으로 처리해야 한다.

    런타임 예외 - 의존 관계에 대한 문제
        런타임 예외는 해당 객체가 처리할 수 없는 예외는 무시하면 된다. 따라서 체크 예외 처럼 예외를 강제로 의존하지 않아도 된다.

    ※ 처음 자바를 설계할 당시에는 체크 예외가 더 나은 선택이라 생각했다. 그래서 자바가 기본으로 제공하는 기능들에는 체크 예외가 많다.
    그런데 시간이 흐르면서 복구 할 수 없는 예외가 너무 많아졌다. 특히 라이브러리를 점점 더 많이 사용하면서 처리해야 하는 예외도 더 늘어났다.
    체크 예외는 해당 라이브러리들이 제공하는 모든 예외를 처리할 수 없을 때마다 throws 에 예외를 덕지덕지 붙어야 했다.
    그래서 개발자들은 throws Exception 이라는 극단적?인 방법도 자주 사용하게 되었다. 물론 이 방법은 사용하면 안된다.
    모든 예외를 던진다고 선언하는 것인데 결과적으로 어떤 예외를 잡고 어떤 예외를 던지는지 알 수 없기 때문이다.
    체크 예외를 사용한다면 잡을 건 잡고 던질 예외는 명확하게 던지도록 선언해야 한다.

    체크 예외의 이런 문제점 때문에 최근 라이브러리들은 대부분 런타임 예외를 기본으로 제공한다.
    사실 위에서 예시로 설명한 JPA 기술도 런타임 예외를 사용한다. 스프링도 대부분 런타임 예외를 제공한다.
    런타임 예외도 필요하면 잡을 수 있기 때문에 필요한 경우에는 잡아서 처리하고, 그렇지 않으면 자연스럽게 던지도록 둔다.
    그리고 예외를 공통으로 처리하는 부분을 앞에 만들어서 처리하면 된다.
    */
    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch(Exception e) {
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch(SQLException e) {
                /*
                기존 예외를 포함해야 스택 트레이스를 확인할 수 있다.(Caused by: java.sql.SQLException: ex)
                예외를 전환할 때는 꼭 기존 예외를 포함해햐 한다.
                */
                //throw new RuntimeSQLException();
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {

        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
