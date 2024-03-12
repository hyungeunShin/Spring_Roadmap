package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {
    /*
    1. 애플리케이션 로직은 DB 드라이버를 통해 커넥션을 조회한다.
    2. DB 드라이버는 DB와 TCP/IP 커넥션을 연결한다. 이 과정에서 3 way handshake 같은 TCP/IP 연결을 위한 네트워크 동작이 발생한다.
    3. DB 드라이버는 TCP/IP 커넥션이 연결되면 ID, PW와 기타 부가정보를 DB에 전달한다.
    4. DB는 ID, PW를 통해 내부 인증을 완료하고, 내부에 DB 세션을 생성한다.
    5. DB는 커넥션 생성이 완료되었다는 응답을 보낸다.
    6. DB 드라이버는 커넥션 객체를 생성해서 클라이언트에 반환한다.
    */
    @Test
    void connection() {
        Connection connection = DBConnectionUtil.getConnection();
        log.info("connection : {}", connection);
        assertThat(connection).isNotNull();
    }
}