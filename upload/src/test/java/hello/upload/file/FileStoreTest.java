package hello.upload.file;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.io.FileInputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class FileStoreTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Test
	void fileUplaod() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "게시판.txt", MediaType.IMAGE_JPEG_VALUE, new FileInputStream("C:/test/테스트.txt"));
		
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(multipart("/upload").file(file)).andExpect(MockMvcResultMatchers.status().isOk());
	}
}
