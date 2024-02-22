package hello.itemservice;

import org.springframework.stereotype.Component;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ItemRepository itemRepository;
    
    private final MemberRepository memberRepository;
    
    @PostConstruct
    public void init() {
    	/*
    	List<String> regions = new ArrayList<>();
    	regions.add("SEOUL");
    	regions.add("JEJU");
        itemRepository.save(new Item("itemA", 10000, 10, true, regions, ItemType.BOOK, "FAST"));
        itemRepository.save(new Item("itemB", 20000, 20, false, regions, ItemType.ETC, "NORMAL"));
        */
    	
    	itemRepository.save(new Item("itemA", 10000, 10));
    	itemRepository.save(new Item("itemB", 20000, 20));
    	
    	Member member = new Member();
    	member.setLoginId("asd");
    	member.setPassword("asd");
    	member.setName("홍길동");
    	
    	memberRepository.save(member);
    }
}