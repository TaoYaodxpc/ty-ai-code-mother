package pers.taoyao.tyaicodemother.ratelimit.enums;

public enum RateLimitType {
    
    /**
     * 接口级别限流
     */
    API,
    
    /**
     * 用户级别限流
     */
    USER,
    
    /**
     * IP 级别限流
     */
    IP
}
