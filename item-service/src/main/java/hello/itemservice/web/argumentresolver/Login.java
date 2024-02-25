package hello.itemservice.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
Target : 어노테이션이 생성될 수 있는 위치 지정. PARAMETER면 메소드의 파라미터로 선언된 객체에서만 사용 가능
Retention : 어노테이션의 메모리 생명 주기 지정. 런타임에도 해당 어노테이션 객체는 메모리에 올라가있음
*/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
	
}
