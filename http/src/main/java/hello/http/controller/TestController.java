package hello.http.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TestController {
	@GetMapping("/event")
	public String getEvent() {
		return "event";
	}
	
	@PostMapping("/event")
	public ResponseEntity<Object> postEvent(HttpServletRequest request) throws Exception {
		System.out.println("postEvent.name : " + request.getParameter("name"));
		System.out.println("postEvent.age : " + request.getParameter("age"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI("/new-event"));
		
		/*
		postEvent.name : 홍길동
		postEvent.age : 123
		new-event.getNewEvent
		getNewEvent.name : null
		getNewEvent.age : null
		*/
		//return new ResponseEntity<Object>("", headers, HttpStatus.MOVED_PERMANENTLY);
		
		/*
		postEvent.name : 홍길동
		postEvent.age : 123
		new-event.postNewEvent
		postNewEvent.name : 홍길동
		postNewEvent.age : 123
		*/
		return new ResponseEntity<Object>("", headers, HttpStatus.PERMANENT_REDIRECT);
		
		/*
		postEvent.name : 홍길동
		postEvent.age : 123
		new-event.getNewEvent
		getNewEvent.name : null
		getNewEvent.age : null
		*/
		//return new ResponseEntity<Object>("", headers, HttpStatus.SEE_OTHER);
	}
	
	@GetMapping("/new-event")
	public String getNewEvent(HttpServletRequest request) {
		System.out.println("new-event.getNewEvent");
		System.out.println("getNewEvent.name : " + request.getParameter("name"));
		System.out.println("getNewEvent.age : " + request.getParameter("age"));
		return "new-event";
	}
	
	@PostMapping("/new-event")
	public ResponseEntity<Object> postNewEvent(HttpServletRequest request) {
		System.out.println("new-event.postNewEvent");
		System.out.println("postNewEvent.name : " + request.getParameter("name"));
		System.out.println("postNewEvent.age : " + request.getParameter("age"));
		
		return new ResponseEntity<Object>("", HttpStatus.OK);
	}
}
