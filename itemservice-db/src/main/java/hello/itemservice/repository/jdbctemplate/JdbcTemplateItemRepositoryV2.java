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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * NamedParameterJdbcTemplate
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {
    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    /*
    1. Map
    2. SqlParameterSource
        - MapSqlParameterSource
            Map 과 유사한데, SQL 타입을 지정할 수 있는 등 SQL에 좀 더 특화된 기능을 제공
        - BeanPropertySqlParameterSource
            자바빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체를 생성한다.
            예) getXxx() -> xxx, getItemName() -> itemName
    */

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) values(:itemName, :price, :quantity)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);

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
