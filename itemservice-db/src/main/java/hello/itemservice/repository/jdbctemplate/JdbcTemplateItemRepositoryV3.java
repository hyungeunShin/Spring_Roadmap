package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SimpleJdbcInsert
 */
@Slf4j
public class JdbcTemplateItemRepositoryV3 implements ItemRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateItemRepositoryV3(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("item")
                .usingGeneratedKeyColumns("id")
                //INSERT SQL에 사용할 컬럼을 지정한다. 특정 값만 저장하고 싶을 때 사용한다. 생략 가능하다.
                //.usingColumns("item_name", "price", "quantity")
        ;
    }

    @Override
    public Item save(Item item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        Number key = jdbcInsert.executeAndReturnKey(param);
        item.setId(key.longValue());
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name = :itemName, price = :price, quantity = :quantity where id = :id";

        SqlParameterSource param = new MapSqlParameterSource()
                                        .addValue("itemName", updateParam.getItemName())
                                        .addValue("price", updateParam.getPrice())
                                        .addValue("quantity", updateParam.getQuantity())
                                        .addValue("id", itemId);
        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select * from item where id = :id";

        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select * from item";
        //동적 쿼리
        if(StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if(StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%', :itemName, '%')";
            andFlag = true;
        }

        if(maxPrice != null) {
            if(andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }

        log.info("findAll query : {}", sql);
        return template.query(sql, param, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        //camelCase 지원
        //BeanPropertyRowMapper 는 ResultSet 의 결과를 받아서 자바빈 규약에 맞추어 데이터를 변환한다.
        return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
