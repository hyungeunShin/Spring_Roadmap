package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

//@Component
@Aspect
public class TimeTraceAop {
	//@Around("execution(* com.example.SpringBoot1..*(..))")
	//@Component로 하면 괜찮은데 SpringConfig를 통해 @Bean으로 등록하면 순환참조 오류 발생
	@Around("execution(* hello.hellospring..service..*(..)) && !target(hello.hellospring.SpringConfig)")
	public Object execute(ProceedingJoinPoint jp) throws Throwable {
		long start = System.currentTimeMillis();
		
		System.out.println("START : " + jp.toString());
		
		try {
			return jp.proceed();
		} finally {
			long finish = System.currentTimeMillis();
			long timeMs = finish - start;
			System.out.println("END : " + jp.toString() + "\n" + "소요시간 : " + timeMs + "ms");
		}
	}
}
