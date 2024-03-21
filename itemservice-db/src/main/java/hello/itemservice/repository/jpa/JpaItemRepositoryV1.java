package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV1 implements ItemRepository {
    /*
    ★ JPA 의 모든 데이터 변경(등록, 수정, 삭제)은 트랜잭션 안에서 이루어져야 한다.
    조회는 트랜잭션이 없어도 가능하다. 변경의 경우 일반적으로 서비스 계층에서 트랜잭션을 시작하기 때문에 문제가 없다.
    하지만 이번 예제에서는 복잡한 비즈니스 로직이 없어서 서비스 계층에서 트랜잭션을 걸지 않았다.
    JPA 에서는 데이터 변경 시 트랜잭션이 필수다. 따라서 리포지토리에 트랜잭션을 걸어주었다.
    다시 한번 강조하지만 일반적으로는 비즈니스 로직을 시작하는 서비스 계층에 트랜잭션을 걸어주는 것이 맞다.


    EntityManager 는 순수한 JPA 기술이고 스프링과는 관계가 없다. 따라서 엔티티 매니저는 예외가 발생하면 JPA 관련 예외를 발생시킨다.
    그렇다면 JPA 예외를 스프링 예외 추상화(DataAccessException)로 어떻게 변환할 수 있을까?

    @Repository 의 기능
        - @Repository 가 붙은 클래스는 컴포넌트 스캔의 대상이 된다.
        - @Repository 가 붙은 클래스는 예외 변환 AOP 의 적용 대상이 된다.
            - 스프링과 JPA 를 함께 사용하는 경우 스프링은 JPA 예외 변환기(PersistenceExceptionTranslator)를 등록한다.
            - 예외 변환 AOP 프록시는 JPA 관련 예외가 발생하면 JPA 예외 변환기를 통해 발생한 예외를 스프링 데이터 접근 예외로 변환한다.
    */
    private final EntityManager em;

    public JpaItemRepositoryV1(EntityManager em) {
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        //JPA 에서 객체를 테이블에 저장할 때는 엔티티 매니저가 제공하는 persist() 메서드를 사용하면 된다.
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        //JPA 는 트랜잭션이 커밋되는 시점에 변경된 엔티티 객체가 있는지 확인한다. 특정 엔티티 객체가 변경된 경우에는 UPDATE SQL 을 실행한다.
        Item item = em.find(Item.class, itemId);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    /*
    JPA 는 JPQL(Java Persistence Query Language)이라는 객체지향 쿼리 언어를 제공한다.
    주로 여러 데이터를 복잡한 조건으로 조회할 때 사용한다.
    SQL 이 테이블을 대상으로 한다면, JPQL 은 엔티티 객체를 대상으로 SQL 을 실행한다 생각하면 된다.

    ★ 엔티티 객체를 대상으로 하기 때문에 from 다음에 Item 엔티티 객체 이름이 들어간다. 엔티티 객체와 속성의 대소문자는 구분해야 한다.
    */
    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i";

        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        if(StringUtils.hasText(itemName) || maxPrice != null) {
            jpql += " where";
        }

        boolean andFlag = false;
        if(StringUtils.hasText(itemName)) {
            jpql += " i.itemName like concat('%', :itemName, '%')";
            andFlag = true;
        }

        if(maxPrice != null) {
            if(andFlag) {
                jpql += " and";
            }
            jpql += " i.price <= :maxPrice";
        }

        log.info("jpql : {}", jpql);
        TypedQuery<Item> query = em.createQuery(jpql, Item.class);

        if(StringUtils.hasText(itemName)) {
            query.setParameter("itemName", itemName);
        }

        if(maxPrice != null) {
            query.setParameter("maxPrice", maxPrice);
        }

        return query.getResultList();
    }
}
