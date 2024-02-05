package hello.http.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Enumeration;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CacheController {
	
	private final ResourceLoader resourceLoader;
	
	@GetMapping("/star")
	public String image() {
		return "image";
	}
	
	/*
	//Last-Modified
	@GetMapping("/download/{img}")
	@ResponseBody
	public ResponseEntity<Object> downloadWithLastModified(@PathVariable(name = "img") String img, HttpServletRequest request) throws IOException {
		System.out.println("이미지 다운로드");
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String element = headerNames.nextElement();
			System.out.println(element + " : " + request.getHeader(element));
		}
		
		Resource resource = resourceLoader.getResource("classpath:/static/" + img);
		InputStream is = resource.getInputStream();
		byte[] array = IOUtils.toByteArray(is);
		is.close();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setLastModified(ZonedDateTime.of(2024, 1, 1, 9, 0, 0, 0, ZoneId.of("Asia/Seoul")));
		
		CacheControl cacheControl = CacheControl.maxAge(Duration.ofSeconds(10)).mustRevalidate();
		headers.setCacheControl(cacheControl);
		
		return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
	}
	*/
	
	//ETag
	@GetMapping("/download/{img}")
	@ResponseBody
	public ResponseEntity<Object> downloadWithETag(@PathVariable(name = "img") String img, HttpServletRequest request) throws IOException {
		System.out.println("이미지 다운로드");
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String element = headerNames.nextElement();
			System.out.println(element + " : " + request.getHeader(element));
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setETag("W/aaaa\"");
		
		CacheControl cacheControl = CacheControl.maxAge(Duration.ofSeconds(10)).mustRevalidate();
		headers.setCacheControl(cacheControl);
		
		if("\"W/aaaa\"\"".equals(request.getHeader("if-none-match"))) {
			return new ResponseEntity<Object>(null, headers, HttpStatus.NOT_MODIFIED);
		} else {
			Resource resource = resourceLoader.getResource("classpath:/static/" + img);
			InputStream is = resource.getInputStream();
			byte[] array = IOUtils.toByteArray(is);
			is.close();
			
			return new ResponseEntity<Object>(array, headers, HttpStatus.OK);
		}
	}
}
