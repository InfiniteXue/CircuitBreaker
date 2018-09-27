package circuitbreaker.state;

import circuitbreaker.AbstractCircuitBreaker;
import circuitbreaker.CircuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 熔断器半开状态
 */
public class HalfOpenState extends AbstractState {

    /**
     * 试探次数
     */
    private AtomicInteger passNum = new AtomicInteger();

    @Override
    public boolean canPass(AbstractCircuitBreaker circuitBreaker) {
        // 判断是否需要切换状态
        switchState(circuitBreaker);
        long threshold = Long.parseLong(circuitBreaker.getPassRateWhenHalfOpen().split(CircuitBreaker.SPLITTER)[0]);
        if (passNum.incrementAndGet() > threshold) {
            // 超过试探次数阈值则该请求不允许通过
            return false;
        }
        return true;
    }

    @Override
    public void switchState(AbstractCircuitBreaker circuitBreaker) {
        long period = Long.parseLong(circuitBreaker.getPassRateWhenHalfOpen().split(CircuitBreaker.SPLITTER)[1]) * 1000;
        long now = System.currentTimeMillis();
        int threshold = circuitBreaker.getFailureThresholdWhenHalfOpen();
        if (now - stateTime > period) {
            if (failureNum.get() > threshold) {
                // 试探期间失败请求次数超过阈值则切换为打开状态
                circuitBreaker.setState(new OpenState());
            } else {
                // 试探期间失败请求次数未超过阈值则切换为关闭状态
                circuitBreaker.setState(new CloseState());
            }
        } else {
            // 未到一个试探周期但失败请求次数已超过阈值则直接切换到打开状态
            if (failureNum.get() > threshold) {
                circuitBreaker.setState(new OpenState());
            }
        }
    }

}
