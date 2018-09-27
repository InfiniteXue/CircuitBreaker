package circuitbreaker.state;

import circuitbreaker.AbstractCircuitBreaker;

/**
 * 熔断器状态接口
 */
public interface State {

    /**
     * 是否允许请求通过
     */
    boolean canPass(AbstractCircuitBreaker circuitBreaker);

    /**
     * 失败请求计数
     */
    void countFailure(AbstractCircuitBreaker circuitBreaker);

    /**
     * 熔断器状态切换
     */
    void switchState(AbstractCircuitBreaker circuitBreaker);

}
