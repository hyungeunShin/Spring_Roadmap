package hello.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class PayConfig {
    /*
    @Profile 은 특정 조건에 따라서 해당 빈을 등록할지 말지 선택한다.
    어디서 많이 본 것 같지 않은가? @Profile 을 타고 들어가면 @Conditional 을 확인할 수 있다.
    */

    @Bean
    @Profile("default")
    public PayClient localPayClient() {
        log.info("LocalPayClient 빈 등록");
        return new LocalPayClient();
    }

    @Bean
    @Profile("prod")
    public PayClient prodPayClient() {
        log.info("ProdPayClient 빈 등록");
        return new ProdPayClient();
    }
}
