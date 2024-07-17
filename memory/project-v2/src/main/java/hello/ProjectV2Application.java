package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectV2Application {
	/*
	스프링 부트가 제공하는 자동 구성 덕분에 복잡한 빈 등록이나 추가 설정 없이 단순하게 라이브러리의 추가 만으로 프로젝트를 편리하게 구성할 수 있다.
	@ConditionalOnXxx 덕분에 라이브러리 설정을 유연하게 제공할 수 있다.
	스프링 부트는 수 많은 자동 구성을 제공한다. 그 덕분에 스프링 라이브러리를 포함해서 수 많은 라이브러리를 편리하게 사용할 수 있다.
	*/

	public static void main(String[] args) {
		SpringApplication.run(ProjectV2Application.class, args);
	}
}
