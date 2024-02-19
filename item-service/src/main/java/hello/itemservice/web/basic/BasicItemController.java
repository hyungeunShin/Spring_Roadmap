package hello.itemservice.web.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
	private final ItemRepository itemRepository;
	
	@GetMapping
	public String items(Model model) {
		List<Item> list = itemRepository.findAll();
		model.addAttribute("items", list);
		return "basic/items";
	}
	
	@GetMapping("/{itemId}")
	public String item(@PathVariable("itemId") Long id, Model model) {
		Item item = itemRepository.findById(id);
		model.addAttribute("item", item);
		return "basic/item";
	}
	
	@GetMapping("/add")
	public String addForm() {
		return "basic/addForm";
	}
	
	/**
	 * @ModelAttribute("item") Item item
	 * model.addAttribute("item", item);이 자동으로 추가
	 * 
	 * @ModelAttribute name 생략 할 시, model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
	 * 
	 * @ModelAttribute까지 생략 가능
	 */
	/*
	새로고침 버튼을 클릭하면 상품이 계속해서 중복 등록
	@PostMapping("/add")
	public String save(Item item) {
		itemRepository.save(item);
		return "basic/item";
	}
	*/
	
	/**
	 * RedirectAttributes를 사용하면 URL 인코딩도 해주고, pathVariable, 쿼리 파라미터까지 처리해준다.
	 * redirect:/basic/items/{itemId}
	 * pathVariable 바인딩: {itemId}
	 * 나머지는 쿼리 파라미터로 처리: ?status=true
	 */
	@PostMapping("/add")
	public String save(Item item, RedirectAttributes ra) {
		itemRepository.save(item);
		
		//URL에 변수를 더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험
		//return "redirect:/basic/items/" + item.getId();
		
		ra.addAttribute("itemId", item.getId());
		ra.addAttribute("status", true);
		
		return "redirect:/basic/items/{itemId}";
	}
	
	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable("itemId") Long id, Model model) {
		Item item = itemRepository.findById(id);
		model.addAttribute("item", item);
		return "basic/editForm";
	}
	
	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable("itemId") Long id, Item item, RedirectAttributes ra) {
		itemRepository.update(id, item);
		
		ra.addAttribute("status", true);
		//컨트롤러에 매핑된 @PathVariable의 값은 redirect에도 사용 가능
		return "redirect:/basic/items/{itemId}";
	}
}
