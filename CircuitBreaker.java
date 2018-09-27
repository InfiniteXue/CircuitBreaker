package circuitbreaker;

/**
 * 熔断器接口
 */
public interface CircuitBreaker {

    String SPLITTER = "/";

    /**
     * 熔断器状态重置
     */
    void reset();

    /**
     * 是否允许请求通过
     */
    boolean canPass();

    /**
     * 失败请求计数
     */
    void countFailure();

}
