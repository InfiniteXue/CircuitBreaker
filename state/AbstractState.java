package circuitbreaker.state;

import circuitbreaker.AbstractCircuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 熔断器状态抽象类
 */
public abstract class AbstractState implements State {

    /**
     * 状态时间戳
     */
    protected volatile long stateTime = System.currentTimeMillis();

    /**
     * 失败请求次数
     */
    protected AtomicInteger failureNum = new AtomicInteger();

    @Override
    public void countFailure(AbstractCircuitBreaker circuitBreaker) {
        failureNum.incrementAndGet();
        switchState(circuitBreaker);
    }

}
