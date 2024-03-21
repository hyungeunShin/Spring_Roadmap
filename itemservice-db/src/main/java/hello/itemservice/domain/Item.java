package hello.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //length = 10 : JPA의 매핑 정보로 DDL(create table)도 생성할 수 있는데, 그때 컬럼의 길이 값으로 활용
    @Column(name = "item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    //JPA 는 public 또는 protected 의 기본 생성자가 필수
    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
