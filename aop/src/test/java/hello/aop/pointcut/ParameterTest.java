package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
class ParameterTest {
    /*
    this, target, args, @target, @within, @annotation, @args 는 포인트컷 표현식을 사용해서 어드바이스에 매개변수를 전달할 수 있다.

    @Before("allMember() && args(arg,..)")
    public void logArgs3(String arg) {
        log.info("[logArgs3] arg={}", arg);
    }

    - 포인트컷의 이름과 매개변수의 이름을 맞추어야 한다. 여기서는 arg 로 맞추었다.
    - 타입이 메서드에 지정한 타입으로 제한된다. 여기서는 메서드의 타입이 String 으로 되어 있기 때문에 다음과 같이 정의되는 것으로 이해하면 된다.
        args(arg,..) args(String,..)
    */

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService proxy : {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        //[logArgs1] String hello.aop.member.MemberServiceImpl.hello(String), arg : helloA
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint jp) throws Throwable {
            Object arg1 = jp.getArgs()[0];
            log.info("[logArgs1] {}, arg : {}", jp.getSignature(), arg1);
            return jp.proceed();
        }

        //[logArgs2] String hello.aop.member.MemberServiceImpl.hello(String), arg : helloA
        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint jp, Object arg) throws Throwable {
            log.info("[logArgs2] {}, arg : {}", jp.getSignature(), arg);
            return jp.proceed();
        }

        //[logArgs3] arg : helloA
        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg : {}", arg);
        }

        //[this] String hello.aop.member.MemberServiceImpl.hello(String), obj : class hello.aop.member.MemberServiceImpl$$SpringCGLIB$$0
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint jp, MemberService obj) {
            log.info("[this] {}, obj : {}", jp.getSignature(), obj.getClass());
        }

        //[target] String hello.aop.member.MemberServiceImpl.hello(String), obj : class hello.aop.member.MemberServiceImpl
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint jp, MemberService obj) {
            log.info("[target] {}, obj : {}", jp.getSignature(), obj.getClass());
        }

        //[@target] String hello.aop.member.MemberServiceImpl.hello(String), obj : @hello.aop.member.annotation.ClassAop()
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint jp, ClassAop annotation) {
            log.info("[@target] {}, obj : {}", jp.getSignature(), annotation);
        }

        //[@within] String hello.aop.member.MemberServiceImpl.hello(String), obj : @hello.aop.member.annotation.ClassAop()
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint jp, ClassAop annotation) {
            log.info("[@within] {}, obj : {}", jp.getSignature(), annotation);
        }

        //[@annotation] String hello.aop.member.MemberServiceImpl.hello(String), annotationValue : test value
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint jp, MethodAop annotation) {
            log.info("[@annotation] {}, annotationValue : {}", jp.getSignature(), annotation.value());
        }
    }
}
