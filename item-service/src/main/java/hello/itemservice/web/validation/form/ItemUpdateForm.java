package hello.itemservice.web.validation.form;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemUpdateForm {
	@NotNull
	private Long id;
	
	@NotBlank
	private String itemName;
	
	@NotNull
	@Range(min = 1000, max = 1000000)
	private Integer price;
	
	private Integer quantity;
}