package hello.springmvc.basic.requestmapping;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MappingController {
	@RequestMapping("/hello-basic")
	public String helloBasic() {
		log.info("helloBasic");
		return "ok";
	}

	@RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
	public String mappingGetV1() {
		log.info("mappingGetV1");
		return "ok";
	}

	@GetMapping(value = "/mapping-get-v2")
	public String mappingGetV2() {
		log.info("mapping-get-v2");
		return "ok";
	}

	@GetMapping("/mapping/{userId}")
	public String mappingPath(@PathVariable("userId") String data) {
		log.info("userId = {}", data);
		return "ok";
	}

	@GetMapping("/mapping/users/{userId}/orders/{orderId}")
	public String mappingPath(@PathVariable("userId") String userId, @PathVariable("orderId") Long orderId) {
		log.info("userId = {}, orderId = {}", userId, orderId);
		return "ok";
	}

	/**
	 * 특정 파라미터 조건 매핑
	 * params = "mode",
	 * params = "!mode"
	 * params = "mode=debug"
	 * params = "mode!=debug"
	 * params = {"mode=debug","data=good"}
	 */
	@GetMapping(value = "/mapping-param", params = "mode=debug")
	public String mappingParam() {
		log.info("mappingParam");
		return "ok";
	}
	
	/**
	 * 특정 헤더로 매핑
	 * headers = "mode",
	 * headers = "!mode"
	 * headers = "mode=debug"
	 * headers = "mode!=debug"
	 */
	@GetMapping(value = "/mapping-header", headers = "mode=debug")
	public String mappingHeader() {
		log.info("mappingHeader");
		return "ok";
	}
	
	/**
	 * Content-Type 헤더 기반 매핑 Media Type
	 * consumes = "application/json"
	 * consumes = "!application/json"
	 * consumes = "application/*"
	 * consumes = "*\/*"
	 * consumes = MediaType.APPLICATION_JSON_VALUE
	 * 
	 * 만약 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환
	 */
	@PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
	//@PostMapping(value = "/mapping-consume", consumes = "application/json")
	public String mappingConsumes() {
		log.info("mappingConsumes");
		return "ok";
	}
	
	/**
	 * Accept 헤더 기반 Media Type
	 * produces = "text/html"
	 * produces = "!text/html"
	 * produces = "text/*"
	 * produces = "*\/*"
	 * produces = MediaType.TEXT_PLAIN_VALUE
	 * 
	 * 만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환
	 */
	@PostMapping(value = "/mapping-produce", produces = "text/html")
	public String mappingProduces() {
		log.info("mappingProduces");
		return "ok";
	}
}
