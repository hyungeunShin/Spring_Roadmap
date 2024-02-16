package hello.itemservice.domain.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemRepositoryTest {
	@Autowired
	private ItemRepository itemRepository;
	
	@AfterEach
	void afterEach() {
		itemRepository.clearStore();
	}
	
	@Test
	void save() {
		//given
		Item item = new Item("itemA", 10000, 10);
		
		//when
		Item saveItem = itemRepository.save(item);
		
		//then
		Item findItem = itemRepository.findById(item.getId());
		assertEquals(saveItem, findItem);
	}
	
	@Test
	void findAll() {
		//given
		Item item1 = new Item("itemA", 10000, 10);
		Item item2 = new Item("itemB", 20000, 20);
		
		itemRepository.save(item1);
		itemRepository.save(item2);
		
		//when
		List<Item> list = itemRepository.findAll();
		
		//then
		assertEquals(2, list.size());
		assertThat(list).contains(item1, item2);
	}
	
	@Test
	void update() {
		//given
		Item item = new Item("itemA", 10000, 10);
		
		Item saveItem = itemRepository.save(item);
		Long id = saveItem.getId();
		
		//when
		Item updateItem = new Item("itemB", 20000, 20);
		itemRepository.update(id, updateItem);
		
		//then
		Item findItem = itemRepository.findById(id);
		assertEquals(updateItem.getItemName(), findItem.getItemName());
		assertEquals(updateItem.getPrice(), findItem.getPrice());
		assertEquals(updateItem.getQuantity(), findItem.getQuantity());
	}
}
