package hello.core.singleton;

public class SingletonService {
	//각 객체들에서 공통적으로 하나의 값이 유지되어야 할 경우
	private static final SingletonService instance = new SingletonService();
	
	public static SingletonService getInstance() {
		return instance;
	}
	
	private SingletonService() {}
	
	public void logic() {
		System.out.println("싱글톤 호출");
	}
}
