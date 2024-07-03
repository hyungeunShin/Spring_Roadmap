package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import hello.proxy.pureproxy.proxy.code.Subject;
import org.junit.jupiter.api.Test;

class ProxyPatternTest {
    @Test
    void noProxy() {
        ProxyPatternClient client = new ProxyPatternClient(new RealSubject());
        client.execute();
        client.execute();
        client.execute();
        /*
        1. client -> realSubject 를 호출해서 값을 조회한다. (1초)
        2. client -> realSubject 를 호출해서 값을 조회한다. (1초)
        3. client -> realSubject 를 호출해서 값을 조회한다. (1초)

        이 데이터가 한번 조회되는 순간 변하지 않는 데이터라면 어딘가에 보관해두고 이미 조회한 데이터를 사용하는 것이 성능상 좋다.
        이런 것을 캐시라고 한다.
        프록시 패턴의 주요 기능은 접근 제어이다. 캐시도 접근 자체를 제어하는 기능 중 하나이다.
        */
    }

    @Test
    void cacheProxy() {
        Subject realSubject = new RealSubject();
        Subject cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
        /*
        1. client 의 cacheProxy 호출 cacheProxy 에 캐시 값이 없다. realSubject 를 호출, 결과를 캐시에 저장 (1초)
        2. client 의 cacheProxy 호출 cacheProxy 에 캐시 값이 있다. cacheProxy 에서 즉시 반환 (0초)
        3. client 의 cacheProxy 호출 cacheProxy 에 캐시 값이 있다. cacheProxy 에서 즉시 반환 (0초)
        */
    }
}
