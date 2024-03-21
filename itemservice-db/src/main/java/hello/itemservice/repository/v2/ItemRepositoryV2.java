package hello.itemservice.repository.v2;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
    /*
    스프링 데이터 JPA 도 스프링 예외 추상화를 지원한다.
    스프링 데이터 JPA 가 만들어주는 프록시에서 이미 예외 변환을 처리하기 때문에 @Repository 와 관계없이 예외가 변환된다.
    */
}
