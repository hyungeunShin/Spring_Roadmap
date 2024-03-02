package hello.upload.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {
	private final FileStore fileStore;
	
	@PostMapping("/upload")
	public ResponseEntity<String> test(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		log.info("test.upload");
		
		if(file.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		fileStore.storeFile(file);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
