package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {
    /*
    @Repository 를 사용하면 JDBC 예외를 스프링의 데이터 계층 예외로 변환해주는 AOP 가 적용된다.
    그런데 스프링이 제공하는 JdbcTemplate 은 JDBC 예외를 스프링의 데이터 계층 예외로 변환해주는 기능을 내부에 포함하고 있다.
    따라서 JdbcTemplate 을 사용할 대는 @Repository 를 사용하지 않았다.
    */
    private final ItemMapper itemMapper;

    @Override
    public Item save(Item item) {
        log.info("itemMapper class : {}", itemMapper.getClass());
        itemMapper.save(item);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        itemMapper.update(itemId, updateParam);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return itemMapper.findAll(cond);
    }
}
