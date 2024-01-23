package hello.hellospring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.hellospring.aop.TimeTraceAop;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;

@Configuration
public class SpringConfig {
	/*
	private DataSource dataSource;
	
	public SpringConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	*/
	
	/*
	private EntityManager em;
	
	public SpringConfig(EntityManager em) {
		this.em = em;
	}
	*/
	
	//스프링 데이터 JPA가 SpringDataJpaMemberRepository 를 스프링 빈으로 자동 등록해준다
	private final MemberRepository memberRepository; 
	
	public SpringConfig(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Bean
	MemberRepository memberRepository() {
		//개방-폐쇄 원칙 : 확장에는 열려있고 수정 또는 변경에는 닫혀있다.
		//스프링의 DI를 활용하여 기존 코드를 전혀 손대지 않고 설정만으로 구현 클래스를 변경
		
		//return new MemoryMemberRepository();
		
		//return new JdbcMemberRepository(dataSource);
		//return new JdbcTemplateMemberRepository(dataSource);
		
		//return new JpaMemberRepository(em);
		
		return this.memberRepository;
	}

    @Bean
    MemberService memberService() {
		return new MemberService(memberRepository());
	}
    
    @Bean
    TimeTraceAop timeTraceAop() {
    	return new TimeTraceAop();
    }
}
