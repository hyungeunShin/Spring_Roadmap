package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
//@Component
public class CommandLineBean {
    /*
    1. ExternalApplication 의 Edit Configurations
    2. Program arguments 추가
        --url=devdb --username=dev_user --password=dev_pw mode=on
    */

    private final ApplicationArguments arguments;

    public CommandLineBean(ApplicationArguments arguments) {
        this.arguments = arguments;
    }

    @PostConstruct
    public void init() {
        log.info("sourceArgs : {}", List.of(arguments.getSourceArgs()));

        Set<String> optionNames = arguments.getOptionNames();
        log.info("optionNames : {}", optionNames);
        for(String optionName : optionNames) {
            log.info("{} : {}", optionName, arguments.getOptionValues(optionName));
        }
    }
}
