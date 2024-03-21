package hello.itemservice;

import hello.itemservice.config.V2Config;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Slf4j
//앞서 설정한 MemoryConfig 를 설정 파일로 사용한다.
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(QuerydslConfig.class)
@Import(V2Config.class)
//컨트롤러만 컴포넌트 스캔을 사용하고 나머지는 직접 수동 등록한다.
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemserviceDbApplication {
	public static void main(String[] args) {
		SpringApplication.run(ItemserviceDbApplication.class, args);
	}

	/*
	특정 프로필의 경우에만 해당 스프링 빈을 등록한다. 여기서는 local 이라는 이름의 프로필이 사용되는 경우에만 testDataInit 이라는 스프링 빈을 등록한다.

	스프링은 로딩 시점에 application.properties 의 spring.profiles.active 속성을 읽어서 프로필로 사용한다.
	이 프로필은 로컬(나의 PC), 운영 환경, 테스트 실행 등등 다양한 환경에 따라서 다른 설정을 할 때 사용하는 정보이다.
	예를 들어서 로컬에서는 로컬 PC에 설치된 데이터베이스에 접근해야 하고 운영 환경에서는 운영 데이터베이스에 접근해야 한다면 서로 설정 정보가 달라야 한다.
	심지어 환경에 따라서 다른 스프링 빈을 등록해야 할 수 도 있다. 프로필을 사용하면 이런 문제를 깔끔하게 해결할 수 있다.
	*/
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

	/*
	H2 데이터베이스는 자바로 개발되어 있고 JVM 안에서 메모리 모드로 동작하는 특별한 기능을 제공한다.
	그래서 애플리케이션을 실행할 때 H2 데이터베이스도 해당 JVM 메모리에 포함해서 함께 실행할 수 있다.
	DB를 애플리케이션에 내장해서 함께 실행한다고 해서 임베디드 모드(Embedded mode)라 한다.
	애플리케이션이 종료되면 임베디드 모드로 동작하는 H2 데이터베이스도 함께 종료되고 데이터도 모두 사라진다.

	메모리 DB는 애플리케이션이 종료될 때 함께 사라지기 때문에 애플리케이션 실행 시점에 데이터베이스 테이블도 새로 만들어주어야 한다.
	JDBC 나 JdbcTemplate 를 직접 사용해서 테이블을 생성하는 DDL 을 호출해도 되지만, 너무 불편하다.
	스프링 부트는 SQL 스크립트를 실행해서 애플리케이션 로딩 시점에 데이터베이스를 초기화하는 기능을 제공한다.(src/test/resources/schema.sql)

	그리고 이 부분 역시 설정하지 않아도 된다.
	src/test/resources/application.properties 에서
	spring.datasource.url, spring.datasource.username, spring.datasource.password= 를 사용하지 않도록 # 을 사용해서 주석처리 했다.
	이렇게 하면 테스트에서 데이터베이스에 접근하는 모든 설정 정보가 사라지게 된다.
	이렇게 별다른 정보가 없으면 스프링 부트는 임베디드 모드로 접근하는 데이터소스(DataSource)를 만들어서 제공한다.
	*/
	/*
	@Bean
	@Profile("test")
	public DataSource dataSource() {
		log.info("메모리 데이터베이스 초기화");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		//임베디드 모드(메모리 모드)로 동작
		//DB_CLOSE_DELAY=-1 : 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끊어지면 데이터베이스도 종료되는데 그것을 방지하는 설정
		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}
	*/
}
