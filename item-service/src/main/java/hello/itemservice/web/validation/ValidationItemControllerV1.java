package hello.itemservice.web.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {
    	Map<String, String> errors = validate(item);
    	
    	if(!errors.isEmpty()) {
    		log.info("errors : {}", errors);
    		model.addAttribute("errors", errors);
    		return "validation/v1/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }
    
    private Map<String, String> validate(Item item) {
    	Map<String, String> errors = new HashMap<>();
    	
    	if(!StringUtils.hasText(item.getItemName())) {
    		errors.put("itemName", "상품 이름은 필수입니다.");
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		errors.put("quantity", "수량은 최대 9,999 까지 허용합니다.");
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			errors.put("globalError", "가격 * 수량은 10,000원 이상이어야 합니다. 현재 값은 " + result + " 입니다.");
    		}
    	}
    	
    	return errors;
    }
}