package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() {
        try {
            //DriverManager 는 라이브러리에 등록된 DB 드라이버들을 관리하고 커넥션을 획득하는 기능을 제공
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection : {}, class : {}", connection, connection.getClass());
            return connection;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
