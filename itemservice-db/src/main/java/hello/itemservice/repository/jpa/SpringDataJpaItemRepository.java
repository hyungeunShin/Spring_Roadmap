package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    /*
    JpaRepository 인터페이스만 상속받으면 스프링 데이터 JPA 가 프록시 기술을 사용해서 구현 클래스를 만들어준다.
    그리고 만든 구현 클래스의 인스턴스를 만들어서 스프링 빈으로 등록한다.
    따라서 개발자는 구현 클래스 없이 인터페이스만 만들면 기본 CRUD 기능을 사용할 수 있다.

    스프링 데이터 JPA 는 메서드 이름을 분석해서 필요한 JPQL 을 만들고 실행해준다.
    조회: find…By , read…By , query…By , get…By
        예:) findHelloBy 처럼 ...에 식별하기 위한 내용(설명)이 들어가도 된다.
    COUNT: count…By 반환타입 long
    EXISTS: exists…By 반환타입 boolean
    삭제: delete…By , remove…By 반환타입 long
    DISTINCT: findDistinct , findMemberDistinctBy
    LIMIT: findFirst3 , findFirst , findTop , findTop3

    https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    */
    List<Item> findByItemNameLike(String itemName);

    List<Item> findByPriceLessThanEqual(Integer price);

    //쿼리 메소드
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

    //쿼리 직접 실행
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
