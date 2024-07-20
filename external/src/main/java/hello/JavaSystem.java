package hello;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSystem {
    public static void print() {
        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url : {}", url);
        log.info("username : {}", username);
        log.info("password : {}", password);
    }
}
