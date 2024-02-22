package hello.itemservice.domain.item;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
//버전 문제 발생
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {
	
	private Long id;
	private String itemName;
	private Integer price;
	private Integer quantity;
	
	private Boolean open;
	private List<String> regions;
	private ItemType itemType;
	private String deliveryCode;
	
	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}

	public Item(String itemName, Integer price, Integer quantity, Boolean open, List<String> regions, ItemType itemType, String deliveryCode) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
		this.open = open;
		this.regions = regions;
		this.itemType = itemType;
		this.deliveryCode = deliveryCode;
	}
	
	/*
	하이버네이트 Validator 관련 링크
	공식 사이트: http://hibernate.org/validator/
	공식 메뉴얼: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/
	검증 애노테이션 모음: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec
	*/
	
	//등록시에 검증할 기능과 수정시에 검증할 기능을 각각 그룹으로 나누어 적용
	//groups를 적용하고 싶으면 @Validated 사용
	//groups 기능은 실제 잘 사용되지는 않는데, 그 이유는 실무에서는 주로 다음에 등장하는 등록용 폼 객체와 수정용 폼 객체를 분리해서 사용
	
	/*
	@NotNull(groups = UpdateCheck.class)
	private Long id;
	
	@NotBlank(message = "공백! {0}", groups = {SaveCheck.class, UpdateCheck.class})
	private String itemName;
	
	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	@Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
	private Integer price;
	
	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	@Max(value = 9999, groups = {SaveCheck.class})
	private Integer quantity;
	
	public Item(String itemName, Integer price, Integer quantity) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
	*/
}
