package hello.core.common;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
//@Scope(value = "request")
//MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class MyLogger {
	private String uuid;
	private String requestURL;
	
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
	public void log(String message) {
		log.info("[" + uuid + "] [" + requestURL + "] " + message);
	}
	
	@PostConstruct
	public void init() {
		this.uuid = UUID.randomUUID().toString();
		log.info("[" + uuid + "] request scope bean create : " + this);
	}
	
	@PreDestroy
	public void close() {
		log.info("[" + uuid + "] request scope bean destroy : " + this);
	}
}
