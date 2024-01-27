package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//@Configuration : 구성정보
//@ComponentScan : @Component 가 붙은 모든 클래스를 스프링 빈으로 등록
@Configuration
@ComponentScan(
	//basePackages = "hello.core",	
		
	//@Configuration은 @Component를 내장하고 있기 때문에 제외
	excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
	/*
	컴포넌트 스캔을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록되기 때문에, 
	AppConfig, TestConfig 등 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 
	그래서 excludeFilters 를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외했다. 
	보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해 제외
	*/
	
	/*
	//자동 빈 vs 수동 빈 테스트
	@Bean(name = "memoryMemberRepository")
	public MemberRepository memberRepository() {
		return new MemoryMemberRepository();
	}
	*/
}
