package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.v2.ItemQuerydslRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceV2 implements ItemService {
    /*
    트레이드 오프
        1. DI, OCP 를 지키기 위해 어댑터를 도입하고, 더 많은 코드를 유지한다.
        2. 어댑터를 제거하고 구조를 단순하게 가져가지만 DI, OCP 를 포기하고 ItemService 코드를 직접 변경한다.

    결국 여기서 발생하는 트레이드 오프는 구조의 안정성 vs 단순한 구조와 개발의 편리성 사이의 선택이다.
    이 둘 중에 하나의 정답만 있을까? 그렇지 않다.
    어떤 상황에서는 구조의 안정성이 매우 중요하고 어떤 상황에서는 단순한 것이 더 나은 선택일 수 있다.
    개발을 할 때는 항상 자원이 무한한 것이 아니다. 그리고 어설픈 추상화는 오히려 독이 되는 경우도 많다.
    무엇보다 추상화도 비용이 든다. 인터페이스도 비용이 든다. 여기서 말하는 비용은 유지보수 관점에서 비용을 뜻한다.
    이 추상화 비용을 넘어설 만큼 효과가 있을 때 추상화를 도입하는 것이 실용적이다.
    */

    //기본 CRUD 는 스프링 데이터 JPA 가 담당하고 복잡한 조회 쿼리는 Querydsl 이 담당
    private final ItemRepositoryV2 itemRepositoryV2;
    private final ItemQuerydslRepositoryV2 itemQuerydslRepositoryV2;

    @Override
    public Item save(Item item) {
        return itemRepositoryV2.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = findById(itemId).orElseThrow();
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepositoryV2.findById(id);
    }

    @Override
    public List<Item> findItems(ItemSearchCond cond) {
        return itemQuerydslRepositoryV2.findAll(cond);
    }
}
