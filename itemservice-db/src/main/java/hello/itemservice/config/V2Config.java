package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.repository.v2.ItemQuerydslRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV2;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class V2Config {
    private final EntityManager em;

    //SpringDataJpa 는 스프링 데이터 JPA 가 프록시 기술로 만들어주고 스프링 빈으로도 등록
    private final ItemRepositoryV2 itemRepositoryV2;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV2(itemRepositoryV2, itemQuerydslRepositoryV2());
    }

    @Bean
    public ItemQuerydslRepositoryV2 itemQuerydslRepositoryV2() {
        return new ItemQuerydslRepositoryV2(em);
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
