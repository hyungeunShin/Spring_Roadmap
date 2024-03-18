package hello.jdbc.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

class CheckedAppTest {
    /*
    Repository 는 SQLException 을 던지고 NetworkClient 는 ConnectException 을 던진다.
    ConnectException 처럼 연결이 실패하거나 SQLException 처럼 데이터베이스에서 발생하는 문제처럼 심각한 문제들은 대부분 애플리케이션 로직에서 처리할 방법이 없다.
    그렇기 때문에 서비스도 SQLException 과 ConnectException 을 던지고 컨트롤러도 처리할 방법이 없기 때문에 예외를 던진다.
    웹 애플리케이션이라면 오류페이지나 ControllerAdvice 를 통해 예외를 공통으로 처리한다.

    2가지 문제
    1. 복구 불가능한 예외
        대부분의 예외는 복구가 불가능하다. 일부 복구가 가능한 예외도 있지만 아주 적다.
        SQLException 을 예를 들면 데이터베이스에 무언가 문제가 있어서 발생하는 예외이다.
        SQL 문법에 문제가 있을 수도 있고, 데이터베이스 자체에 뭔가 문제가 발생했을 수도 있다.
        데이터베이스 서버가 중간에 다운 되었을 수도 있다. 이런 문제들은 대부분 복구가 불가능하다.
        특히나 대부분의 서비스나 컨트롤러는 이런 문제를 해결할 수는 없다. 따라서 이런 문제들은 일관성 있게 공통으로 처리해야 한다.

    2. 의존 관계에 대한 문제
        컨트롤러에서 SQLException 을 의존하는 것이 문제가 된다.
        예를 들어 Repository 를 JDBC 가 아닌 JPA 로 변경해서 SQLException 이 아닌 JPAException 으로 예외가 변경되면 어떻게 하는가?
        SQLException 에 의존하던 모든 서비스, 컨트롤러의 코드를 수정해야 하는 일이 생긴다.

        그렇다면 최상위 예외인 Exception 을 던지면 문제가 해결되지 않을까?
        Exception 은 물론이고 그 하위 타입인 SQLException, ConnectException 도 함께 던지게 된다.
        코드가 깔끔해지는 것 같지만 Exception 은 최상위 타입이므로 모든 체크 예외를 다 밖으로 던지는 문제가 발생한다.

        결과적으로 체크 예외의 최상위 타입인 Exception 을 던지게 되면 다른 체크 예외를 체크할 수 있는 기능이 무효화되고 중요한 체크 예외를 다 놓치게 된다.
        중간에 중요한 체크 예외가 발생해도 컴파일러는 Exception 을 던지기 때문에 문법에 맞다고 판단해서 컴파일 오류가 발생하지 않는다.
        이렇게 하면 모든 예외를 다 던지기 때문에 체크 예외를 의도한 대로 사용하는 것이 아니다.
        따라서 꼭 필요한 경우가 아니면 이렇게 Exception 자체를 밖으로 던지는 것은 좋지 않은 방법이다.
    */
    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("DB 오류");
        }
    }
}
