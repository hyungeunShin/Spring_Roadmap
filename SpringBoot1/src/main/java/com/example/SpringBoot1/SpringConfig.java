package com.example.SpringBoot1;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.SpringBoot1.repository.JdbcMemberRepository;
import com.example.SpringBoot1.repository.MemberRepository;
import com.example.SpringBoot1.service.MemberService;

@Configuration
public class SpringConfig {
	private DataSource dataSource;
	
	public SpringConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	MemberRepository memberRepository() {
		//개방-폐쇄 원칙 : 확장에는 열려있고 수정 또는 변경에는 닫혀있다.
		//스프링의 DI를 활용하여 기존 코드를 전혀 손대지 않고 설정만으로 구현 클래스를 변경
		//return new MemoryMemberRepository();
		return new JdbcMemberRepository(dataSource);
	}

    @Bean
    MemberService memberService() {
		return new MemberService(memberRepository());
	}
}
