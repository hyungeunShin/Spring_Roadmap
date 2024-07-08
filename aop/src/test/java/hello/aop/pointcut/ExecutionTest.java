package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ExecutionTest {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod : {}", helloMethod);
    }

    @Test
    @DisplayName("가장 정확한 포인트컷")
    void exactMatch() {
        /*
        execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
            - 메소드 실행 조인 포인트를 매칭
            - ? 는 생략 가능
            - * 같은 패턴 지정 가능

        접근제어자?: public
        반환타입: String
        선언타입?: hello.aop.member.MemberServiceImpl
        메서드이름: hello
        파라미터: (String)
        예외?: 생략
        */
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        //MemberServiceImpl.hello(String) 메서드와 포인트컷 표현식의 모든 내용이 정확하게 일치한다. 따라서 true 를 반환한다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("가장 많이 생략한 포인트컷")
    void allMatch() {
        /*
        접근제어자?: 생략
        반환타입: *
        선언타입?: 생략
        메서드이름: *
        파라미터: (..)
        예외?: 없음

        * 은 아무 값이 들어와도 된다는 뜻이다.
        파라미터에서 .. 은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다. ( 0..* )
        */
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름 매칭 포인트컷1")
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름 매칭 포인트컷2")
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름 매칭 포인트컷3")
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("메서드 이름 매칭 포인트컷4")
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지 매칭 포인트컷1")
    void packageExactMatch1() {
        /*
        hello.aop.member.*(1).*(2)
            (1): 타입
            (2): 메서드 이름
        */
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 매칭 포인트컷2")
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 매칭 포인트컷3")
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("패키지 매칭 포인트컷4")
    void packageMatchSubPackage1() {
        /*
        . : 정확하게 해당 위치의 패키지
        .. : 해당 위치의 패키지와 그 하위 패키지도 포함
        */
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("패키지 매칭 포인트컷5")
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("부모 타입 매칭 포인트컷1")
    void typeExactMatch() {
        //타입 정보가 정확하게 일치하기 때문에 매칭
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("부모 타입 매칭 포인트컷2")
    void typeMatchSuperType() {
        //execution 에서는 MemberService 처럼 부모 타입을 선언해도 그 자식 타입은 매칭
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("부모 타입 매칭 포인트컷3")
    void typeMatchInternal() throws NoSuchMethodException {
        //MemberServiceImpl 를 표현식에 선언했기 때문에 그 안에 있는 internal(String) 메서드도 매칭
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("부모 타입 매칭 포인트컷4")
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        //포인트컷으로 지정한 MemberService 는 internal 이라는 이름의 메서드가 없다.
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("파라미터 매칭 포인트컷 1")
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 포인트컷 2")
    void argsMatchNoArgs() {
        //파라미터가 없어야 함
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("파라미터 매칭 포인트컷 3")
    void argsMatchStar() {
        //정확히 하나의 파라미터 허용, 모든 타입 허용
        //(*, *) : 정확히 두 개의 파라미터, 단 모든 타입을 허용
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 포인트컷 4")
    void argsMatchAll() {
        //숫자와 무관하게 모든 파라미터, 모든 타입 허용
        //파라미터가 없어도 된다. 0..* 로 이해
        //(), (Xxx), (Xxx, Xxx)
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("파라미터 매칭 포인트컷 5")
    void argsMatchComplex() {
        //String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
        //(String), (String, Xxx), (String, Xxx, Xxx)
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
