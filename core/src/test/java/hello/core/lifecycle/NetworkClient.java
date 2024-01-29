package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient { //implements InitializingBean, DisposableBean {
	private String url;

	public NetworkClient() {
		System.out.println("생성자 호출, url : " + url);
		//connect();
		//call("초기화 연결 메시지");
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	//서비스 시작 시 호출
	public void connect() {
		System.out.println("connect : " + url);
	}
	
	public void call(String msg) {
		System.out.println("message : " + msg);
	}
	
	//서비스 종료 시 호출
	public void disconnect() {
		System.out.println("disconnect : " + url);
	}
	
	//1. 인터페이스를 활용
	/*
	//implements InitializingBean
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet");
		connect();
		call("초기화 연결 메시지");
	}
	
	//implements DisposableBean
	@Override
	public void destroy() throws Exception {
		System.out.println("destroy");
		disconnect();
	}
	*/
	
	//2. 설정 정보에 초기화 메서드, 종료 메서드 지정 => @Bean(initMethod = "init", destroyMethod = "close") 
	/*
	public void init() {
		System.out.println("init");
		connect();
		call("초기화 연결 메시지");
	}
	
	public void close() {
		System.out.println("close");
		disconnect();
	}
	*/
	
	//3. @PostConstruct, @PreDestroy 어노테이션
	@PostConstruct
	public void init() {
		System.out.println("init");
		connect();
		call("초기화 연결 메시지");
	}
	
	@PreDestroy
	public void close() {
		System.out.println("close");
		disconnect();
	}
}
