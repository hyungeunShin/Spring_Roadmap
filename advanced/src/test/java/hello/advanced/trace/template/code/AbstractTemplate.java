package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {
    //템플릿이라는 틀에 변하지 않는 부분을 몰아둔다. 그리고 일부 변하는 부분을 별도로 호출해서 해결한다.
    //템플릿 메서드 패턴은 부모 클래스에 변하지 않는 템플릿 코드를 둔다. 그리고 변하는 부분은 자식 클래스에 두고 상속과 오버라이딩을 사용해서 처리한다.

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call();
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        log.info("resultTime : {}", endTime - startTime);
    }

    protected abstract void call();
}
