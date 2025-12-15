package pers.taoyao.tyaicodemother.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 * @date 2025/12/7
 * @Version v1.0
 * @description
 */
@RestController("health")
public class HealthController {
    @GetMapping("/ok")
    public String health() {
        return "ok";
    }
}
