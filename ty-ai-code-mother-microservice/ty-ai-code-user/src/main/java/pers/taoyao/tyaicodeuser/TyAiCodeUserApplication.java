package pers.taoyao.tyaicodeuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("pers.taoyao.tyaicodeuser.mapper")
@ComponentScan("pers.taoyao")
public class TyAiCodeUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TyAiCodeUserApplication.class, args);
    }
}
