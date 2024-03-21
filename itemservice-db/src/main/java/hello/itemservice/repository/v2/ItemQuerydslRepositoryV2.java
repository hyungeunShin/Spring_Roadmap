package hello.itemservice.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.domain.QItem;
import hello.itemservice.repository.ItemSearchCond;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class ItemQuerydslRepositoryV2 {
    private final JPAQueryFactory query;

    public ItemQuerydslRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        //QItem 은 static import 가능
        return query.select(QItem.item)
                .from(QItem.item)
                .where(likeItemName(itemName), maxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName) {
        if(StringUtils.hasText(itemName)) {
            return QItem.item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer maxPrice) {
        if(maxPrice != null) {
            return QItem.item.price.loe(maxPrice);
        }
        return null;
    }
}
