package hello.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class DbConfigTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    TransactionManager transactionManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void checkBean() throws SQLException {
        /*
        DbConfig.class 에 @Configuration 을 주석처리해도 스프링 빈으로 등록이 된다.
        이 빈들은 모두 스프링 부트가 자동으로 등록해 준 것이다.

        JdbcTemplateAutoConfiguration : JdbcTemplate
        DataSourceAutoConfiguration : DataSource
        DataSourceTransactionManagerAutoConfiguration : TransactionManager
        */
        log.info("dataSource : {}", dataSource);
        log.info("transactionManager : {}", transactionManager);
        log.info("jdbcTemplate : {}", jdbcTemplate);

        log.info("dataSource.getConnection() : {}", dataSource.getConnection());

        assertThat(dataSource).isNotNull();
        assertThat(transactionManager).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
    }
}