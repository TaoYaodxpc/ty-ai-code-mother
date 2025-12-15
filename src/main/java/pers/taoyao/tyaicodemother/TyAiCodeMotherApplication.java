package pers.taoyao.tyaicodemother;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class TyAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(TyAiCodeMotherApplication.class, args);
    }

}
