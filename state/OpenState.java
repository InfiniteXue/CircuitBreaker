package circuitbreaker.state;

import circuitbreaker.AbstractCircuitBreaker;

/**
 * 熔断器打开状态
 */
public class OpenState extends AbstractState {

    @Override
    public boolean canPass(AbstractCircuitBreaker circuitBreaker) {
        // 判断是否需要切换到半开状态
        switchState(circuitBreaker);
        // 打开状态下所有请求都不允许通过
        return false;
    }

    @Override
    public void countFailure(AbstractCircuitBreaker circuitBreaker) {
        // 失败请求计数空实现
    }

    @Override
    public void switchState(AbstractCircuitBreaker circuitBreaker) {
        long waitTime = circuitBreaker.getWaitTimeFromOpenToHalfOpen() * 1000;
        long now = System.currentTimeMillis();
        if (now - stateTime > waitTime) {
            // 超过等待时间切换到半开状态
            circuitBreaker.setState(new HalfOpenState());
        }
    }

}
