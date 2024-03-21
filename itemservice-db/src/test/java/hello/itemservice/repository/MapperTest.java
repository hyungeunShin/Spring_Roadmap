package hello.itemservice.repository;

import hello.itemservice.repository.mybatis.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootTest
class MapperTest {
    /*
    1. 애플리케이션 로딩 시점에 MyBatis 스프링 연동 모듈은 @Mapper 가 붙어있는 인터페이스를 찾는다.
    2. 해당 인터페이스가 발견되면 동적 프록시 기술을 사용해서 ItemMapper 인터페이스의 구현체를 만든다.
    3. 생성된 구현체를 스프링 빈으로 등록한다.
    */
    @Autowired
    ApplicationContext context;

    @Autowired
    ItemMapper mapper;

    @Test
    void findMapper() {
        ItemMapper bean = context.getBean(ItemMapper.class);
        log.info("result : {}", bean.getClass());
        log.info("mapper class : {}", mapper.getClass());
    }
}
