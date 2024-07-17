package memory;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryFinder {
    public Memory get() {
        //JVM 이 사용할 수 있는 최대 메모리
        long max = Runtime.getRuntime().maxMemory();

        //JVM 이 확보한 전체 메모리(JVM 은 처음부터 max 까지 다 확보하지 않고 필요할 때 마다 조금씩 확보)
        long total = Runtime.getRuntime().totalMemory();

        //total 중에 사용하지 않은 메모리(JVM 이 확보한 전체 메모리 중에 사용하지 않은 것)
        long free = Runtime.getRuntime().freeMemory();

        long used = total - free;

        return new Memory(used, max);
    }

    @PostConstruct
    public void init() {
        log.info("init memoryFinder");
    }
}
