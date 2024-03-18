package hello.jdbc.exception.translator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
class SpringExceptionTranslatorTest {
    /*
    스프링 데이터 접근 예외 계층의 최고 상위는 org.springframework.dao.DataAccessException 이다.
    DataAccessException 은 RuntimeException 을 상속 받기 때문에 데이터 접근 계층의 모든 예외는 RuntimeException 이다.

    DataAccessException 은 크게 2가지로 구분하는데 NonTransient 예외와 Transient 예외이다.
        - Transient 는 일시적이라는 뜻이다. Transient 하위 예외는 동일한 SQL 을 다시 시도했을 때 성공할 가능성이 있다.
          예를 들어서 쿼리 타임아웃, 락과 관련된 오류들이다. 이런 오류들은 데이터베이스 상태가 좋아지거나 락이 풀렸을 때 다시 시도하면 성공할 수 도 있다.

        - NonTransient 는 일시적이지 않다는 뜻이다. 같은 SQL 을 그대로 반복해서 실행하면 실패한다.
          예를 들면 SQL 문법 오류, 데이터베이스 제약조건 위배 등이 있다.
    */

    DataSource dataSource;

    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammer";

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeQuery();
        } catch(SQLException e) {
            /*
            직접 예외를 확인하고 하나하나 스프링이 만들어준 예외로 변환하는 것은 현실성이 없다.
            이렇게 하려면 해당 오류 코드를 확인하고 스프링의 예외 체계에 맞추어 예외를 직접 변환해야 할 것이다.
            그리고 데이터베이스마다 오류 코드가 다르다는 점도 해결해야 한다.
            */
            assertThat(e.getErrorCode()).isEqualTo(42122);
            int errorCode = e.getErrorCode();
            log.info("errorCode : {}", errorCode, e);
        }
    }

    /*
    스프링은 데이터 접근 계층에 대한 일관된 예외 추상화를 제공한다.
    스프링은 예외 변환기를 통해서 SQLException 의 ErrorCode 에 맞는 적절한 스프링 데이터 접근 예외로 변환해준다.
    서비스, 컨트롤러 계층에서 예외 처리가 필요하면 특정 기술에 종속적인 SQLException 같은 예외를 직접 사용하는 것이 아니라, 스프링이 제공하는 데이터 접근 예외를 사용하면 된다.

    DB 마다 SQL ErrorCode 는 다르다. 그런데 스프링은 어떻게 각각의 DB가 제공하는 SQL ErrorCode 까지 고려해서 예외를 변환할 수 있을까?
    비밀은 org.springframework.jdbc.support.sql-error-codes.xml 에 있다.
    스프링 SQL 예외 변환기는 SQL ErrorCode 를 이 파일에 대입해서 어떤 스프링 데이터 접근 예외로 전환해야 할 지 찾아낸다.
    */
    @Test
    void exceptionTranslator() {
        String sql = "select bad grammer";

        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.executeQuery();
        } catch(SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(42122);
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            //첫번째 파라미터는 읽을 수 있는 설명이고, 두번째는 실행한 sql, 마지막은 발생된 SQLException 을 전달하면 된다.
            DataAccessException resultEx = translator.translate("select", sql, e);
            log.info("resultEx", resultEx);
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
