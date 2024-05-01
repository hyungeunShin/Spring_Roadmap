package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class TemplateMethodTest {
    @Test
    void templateMethodV0() {
        logic1();
        logic2();
    }

    @Test
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();
        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
    }
    
    @Test
    void templateMethodV2() {
        //익명 내부 클래스 사용
        //템플릿 메서드 패턴은 SubClassLogic1, SubClassLogic2 처럼 클래스를 계속 만들어야 하는 단점이 있다.
        //익명 내부 클래스를 사용하면 이런 단점을 보완할 수 있다.
        AbstractTemplate template1 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("비즈니스 로직 실행");      
            }
        };
        log.info("클래스 이름1 : {}", template1.getClass());
        template1.execute();

        AbstractTemplate template2 = new AbstractTemplate() {
            @Override
            protected void call() {
                log.info("한번 더 실행");
            }
        };
        log.info("클래스 이름1 : {}", template2.getClass());
        template2.execute();
    }
    
    private void logic1() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직1 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        log.info("resultTime : {}", endTime - startTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        log.info("비즈니스 로직2 실행");
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        log.info("resultTime : {}", endTime - startTime);
    }
}
