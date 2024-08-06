package hello.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class TrafficController {
    private List<String> list = new ArrayList<>();

    @Autowired
    DataSource dataSource;
    
    /*
    CPU 사용량 대시보드 확인
    */
    @GetMapping("/cpu")
    public String cpu() {
        log.info("cpu");

        long value = 0;
        for(long i = 0; i < 100000000000L; i++) {
            value++;
        }
        return "cpu value : " + value;
    }

    /*
    JVM 메모리 사용량 대시보드 확인

    JVM 메모리 사용량이 계속 증가하다가 최대치를 넘는 순간 메트릭이 잡히지 않는다.
    JVM 내부에서 OutOfMemory 가 발생했기 때문이다.
    기다려보면 애플리케이션 로그에서 다음과 같은 오류를 확인할 수 있다.
        java.lang.OutOfMemoryError: Java heap space
    */
    @GetMapping("/jvm")
    public String jvm() {
        log.info("jvm");
        for(long i = 0; i < 1000000; i++) {
            list.add("jvm" + i);
        }
        return "jvm";
    }

    /*
    Active 커넥션이 커넥션 풀의 최대 숫자인 10개를 넘어가게 되면 커넥션을 획득하기 위해 대기(Pending)하게 된다.
    그래서 커넥션 획득 부분에서 쓰레드가 대기하게 되고 결과적으로 HTTP 요청을 응답하지 못한다.

    DB 커넥션을 획득하기 위해 대기하던 톰캣 쓰레드가 30초 이상 DB 커넥션을 획득하지 못하면 다음과 같은 예외가 발생하면서 커넥션 획득을 포기한다.
    Connection is not available, request timed out after 30015ms
    */
    @GetMapping("/jdbc")
    public String jdbc() throws SQLException {
        log.info("jdbc");
        Connection connection = dataSource.getConnection();
        log.info("connection : {}", connection);
        return "jdbc";
    }

    /*
    ERROR Logs, logback_events_total 메트릭에서 ERROR 로그가 급증하는 것을 확인
    */
    @GetMapping("/error-log")
    public String errorLog() {
        log.error("error log");
        return "error";
    }
}
