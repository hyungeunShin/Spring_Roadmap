package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }
    
    //BindingResult bindingResult 파라미터의 위치는 @ModelAttribute Item item 다음에 와야 한다.
    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	if(!StringUtils.hasText(item.getItemName())) {
    		/*
        	public FieldError(String objectName, String field, String defaultMessage)
        	
        	objectName : @ModelAttribute 이름
        	field : 오류가 발생한 필드 이름
        	defaultMessage : 오류 기본 메시
        	*/
    		bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			/*
    			public ObjectError(String objectName, @Nullable String defaultMessage)
    			
    			objectName : @ModelAttribute 이름
				defaultMessage : 오류 기본 메시지
    			*/
    			bindingResult.addError(new ObjectError("item", "가격 * 수량은 10,000원 이상이어야 합니다. 현재 값은 " + result + " 입니다."));
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		//BindingResult 는 Model에 자동으로 포함된다.
    		return "validation/v2/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    
    //오류가 발생하는 경우 고객이 입력한 내용이 사라지는 문제 해결
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	if(!StringUtils.hasText(item.getItemName())) {
    		/*
    		public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
				@Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
			
	    	objectName : 오류가 발생한 객체 이름
	    	field : 오류 필드
	    	rejectedValue : 사용자가 입력한 값(거절된 값)
	    	bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
	    	codes : 메시지 코드
	    	arguments : 메시지에서 사용하는 인자
	    	defaultMessage : 기본 오류 메시지
    		 */
    		bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			/*
    			public ObjectError(String objectName, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
    			*/
    			bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량은 10,000원 이상이어야 합니다. 현재 값은 " + result + " 입니다."));
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v2/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	if(!StringUtils.hasText(item.getItemName())) {
    		bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] {"required.item.itemName", "required.default"}, null, null));
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"}, new Object[] {1000, 1000000}, null));
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[] {"max.item.quantity"}, new Object[] {9999}, null));
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			bindingResult.addError(new ObjectError("item", new String[] {"totalPriceMin"}, new Object[] {10000, result}, null));
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v2/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    
    /*
    rejectValue(), reject() 는 내부에서 MessageCodesResolver 를 사용한다. 여기에서 메시지 코드들을 생성한다.
	FieldError, ObjectError 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다.
	MessageCodesResolver 를 통해서 생성된 순서대로 오류 코드를 보관한다. 
    */
    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
    	//컨트롤러에서 BindingResult 는 검증해야 할 객체인 target 바로 다음에 온다. 따라서 BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있다.
    	log.info("objectName = {}", bindingResult.getObjectName());
    	log.info("target = {}", bindingResult.getTarget());
    	
    	if(!StringUtils.hasText(item.getItemName())) {
    		/*
    		void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
    		
    		field : 오류 필드명
			errorCode : 오류 코드(이 오류 코드는 메시지에 등록된 코드가 아니다. 뒤에서 설명할 messageResolver를 위한 오류 코드이다.)
			errorArgs : 오류 메시지에서 {0} 을 치환하기 위한 값
			defaultMessage : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지
    		*/
    		bindingResult.rejectValue("itemName", "required");
    		
    		/*
    		BindingResult가 rejectValue() 메소드로 MessageCodesResolver를 통해서 내부적으로 메시지 코드를 생성
    		required.item.itemName
    		required.itemName
    		required.java.lang.String
    		required
    		*/
    	}
    	
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.rejectValue("price", "range", new Object[] {1000, 1000000}, null);
    	}
    	
    	if(item.getQuantity() == null || item.getQuantity() >= 9999) {
    		bindingResult.rejectValue("quantity", "max", new Object[] {9999}, null);
    	}
    	
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int result = item.getPrice() * item.getQuantity();
    		
    		if(result < 10000) {
    			//void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
    			bindingResult.reject("totalPriceMin", new Object[] {10000, result}, null);
    			
    			/*
    			totalPriceMin.item
				totalPriceMin
    			*/
    		}
    	}
    	
    	if(bindingResult.hasErrors()) {
    		log.info("errors = {}", bindingResult);
    		return "validation/v2/addForm";
    	}
    	
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }
}