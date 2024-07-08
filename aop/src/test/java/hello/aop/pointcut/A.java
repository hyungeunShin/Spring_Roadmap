package hello.aop.pointcut;

public class A {
    /*
    AspectJ 는 포인트컷을 편리하게 표현하기 위한 특별한 표현식을 제공한다.
    포인트컷 표현식은 AspectJ pointcut expression 즉 AspectJ 가 제공하는 포인트컷 표현식을 줄여서 말하는 것이다.

    포인트컷 지시자
        포인트컷 표현식은 execution 같은 포인트컷 지시자(Pointcut Designator)로 시작한다. 줄여서 PCD 라 한다.

        - execution : 메소드 실행 조인 포인트를 매칭한다. 스프링 AOP 에서 가장 많이 사용하고, 기능도 복잡하다.
        - within : 특정 타입 내의 조인 포인트를 매칭한다.
        - args : 인자가 주어진 타입의 인스턴스인 조인 포인트
        - this : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
        - target : Target 객체(스프링 AOP 프록시가 가리키는 실제 대상)를 대상으로 하는 조인 포인트
        - @target : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
        - @within : 주어진 애노테이션이 있는 타입 내 조인 포인트
        - @annotation : 메서드가 주어진 애노테이션을 가지고 있는 조인 포인트를 매칭
        - @args : 전달된 실제 인수의 런타임 타입이 주어진 타입의 애노테이션을 갖는 조인 포인트
        - bean : 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정한다.
    */
}
