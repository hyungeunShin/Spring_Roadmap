package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v1_proxy.ConcreteProxyConfig;
import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import hello.proxy.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV1;
import hello.proxy.config.v3_proxyfactory.ProxyFactoryConfigV2;
import hello.proxy.config.v4_postprocessor.BeanPostProcessorConfig;
import hello.proxy.config.v5_autoproxy.AutoProxyConfig;
import hello.proxy.config.v6_aop.AopConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import({AppV1Config.class, AppV2Config.class})	//클래스를 스프링 빈으로 등록
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicProxyFilterConfig.class)
//@Import(ProxyFactoryConfigV1.class)
//@Import(ProxyFactoryConfigV2.class)
//@Import(BeanPostProcessorConfig.class)
//@Import(AutoProxyConfig.class)
@Import(AopConfig.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app.v3")
public class ProxyApplication {
	/*
    기존 요구사항
        (완료)- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력
        (완료)- 애플리케이션의 흐름을 변경하면 안됨
            (완료)- 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨
        (완료)- 메서드 호출에 걸린 시간
        (완료)- 정상 흐름과 예외 흐름 구분
            (완료)- 예외 발생시 예외 정보가 남아야 함
        (완료)- 메서드 호출의 깊이 표현
        (완료)- HTTP 요청을 구분
            (완료)- HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
            (완료)- 트랜잭션 ID(DB 트랜잭션 X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

    요구사항 추가
        - 원본 코드를 전혀 수정하지 않고, 로그 추적기를 적용해라.
        - 특정 메서드는 로그를 출력하지 않는 기능
            - 보안상 일부는 로그를 출력하면 안된다.
        - 다음과 같은 다양한 케이스에 적용할 수 있어야 한다.
            - v1 : 인터페이스가 있는 구현 클래스에 적용
            - v2 : 인터페이스가 없는 구체 클래스에 적용
            - v3 : 컴포넌트 스캔 대상에 기능 적용

    가장 어려운 문제는 원본 코드를 전혀 수정하지 않고 로그 추적기를 도입하는 것이다.
    이 문제를 해결하려면 프록시(Proxy)를 사용해야 한다.

    ※ 프록시(Proxy)
    - 클라이언트와 서버
        클라이언트(Client)와 서버(Server)라고 하면 개발자들은 보통 서버 컴퓨터를 생각한다.
        사실 클라이언트와 서버의 개념은 상당히 넓게 사용된다.
        클라이언트는 의뢰인이라는 뜻이고 서버는 '서비스나 상품을 제공하는 사람이나 물건'을 뜻한다.
        따라서 클라이언트와 서버의 기본 개념을 정의하면 클라이언트는 서버에 필요한 것을 요청하고 서버는 클라이언트의 요청을 처리하는 것이다.
        이 개념을 우리가 익숙한 컴퓨터 네트워크에 도입하면 클라이언트는 웹 브라우저가 되고 요청을 처리하는 서버는 웹 서버가 된다.
        이 개념을 객체에 도입하면 요청하는 객체는 클라이언트가 되고 요청을 처리하는 객체는 서버가 된다.

    - 직접 호출과 간접 호출
        클라이언트와 서버 개념에서 일반적으로 클라이언트가 서버를 직접 호출하고, 처리 결과를 직접 받는다. 이것을 직접 호출이라 한다.
        그런데 클라이언트가 요청한 결과를 서버에 직접 요청하는 것이 아니라 어떤 대리자를 통해서 대신 간접적으로 서버에 요청할 수 있다.
        예를 들어서 내가 직접 마트에서 장을 볼 수도 있지만, 누군가에게 대신 장을 봐달라고 부탁할 수도 있다.
        여기서 대신 장을 보는 대리자를 영어로 프록시(Proxy)라 한다.

        예시
        직접 호출과 다르게 간접 호출을 하면 대리자가 중간에서 여러가지 일을 할 수 있다는 점이다.
            - 엄마에게 라면을 사달라고 부탁 했는데, 엄마는 그 라면은 이미 집에 있다고 할 수도 있다.
              그러면 기대한 것 보다 더 빨리 라면을 먹을 수 있다. (접근 제어, 캐싱)
            - 아버지께 자동차 주유를 부탁했는데, 아버지가 주유 뿐만 아니라 세차까지 하고 왔다.
              클라이언트가 기대한 것 외 에 세차라는 부가 기능까지 얻게 되었다. (부가 기능 추가)
            - 그리고 대리자가 또 다른 대리자를 부를 수도 있다.
              예를 들어서 내가 동생에게 라면을 사달라고 했는데, 동생은 또 다른 누군가에게 라면을 사달라고 다시 요청할 수도 있다.
              중요한 점은 클라이언트는 대리자를 통해서 요청했기 때문에 그 이후 과정은 모른다는 점이다.
              동생을 통해서 라면이 나에게 도착하기만 하면 된다. (프록시 체인)

    - 대체 가능
        그런데 여기까지 듣고 보면 아무 객체나 프록시가 될 수 있는 것 같다.
        객체에서 프록시가 되려면 클라이언트는 서버에게 요청을 한 것인지 프록시에게 요청을 한 것인지 조차 몰라야 한다.
        쉽게 이야기해서 서버와 프록시는 같은 인터페이스를 사용해야 한다.
        그리고 클라이언트가 사용하는 서버 객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작할 수 있어야 한다.
        즉, 클라이언트는 서버 인터페이스(ServerInterface)에만 의존한다.
        그리고 서버와 프록시가 같은 인터페이스를 사용한다. 따라서 DI를 사용해서 대체 가능하다.

    - 프록시의 주요 기능
        프록시를 통해서 할 수 있는 일은 크게 2가지로 구분할 수 있다.
        - 접근 제어
            - 권한에 따른 접근 차단
            - 캐싱
            - 지연 로딩
        - 부가 기능 추가
            - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
            - 예) 요청 값이나, 응답 값을 중간에 변형한다.
            - 예) 실행 시간을 측정해서 추가 로그를 남긴다.

    - 프록시 패턴과 데코레이터 패턴 둘 다 프록시를 사용하는 방법이지만 의도(intent)에 따라서 프록시 패턴과 데코레이터 패턴으로 구분한다.
        - 프록시 패턴: 접근 제어가 목적
        - 데코레이터 패턴: 새로운 기능 추가가 목적
    */

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}
}
