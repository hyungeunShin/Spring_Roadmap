package hello.jdbc.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Member {
    private String memberId;
    private int money;
}
