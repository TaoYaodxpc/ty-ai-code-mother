package pers.taoyao.tyaicodemother;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class TyAiCodeScreenshotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TyAiCodeScreenshotApplication.class, args);
    }
}
