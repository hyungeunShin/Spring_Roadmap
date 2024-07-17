package memory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Memory {
    private long used;  //사용중인 메모리
    private long max;   //최대 메모리
}
