package circuitbreaker.state;

import circuitbreaker.AbstractCircuitBreaker;
import circuitbreaker.CircuitBreaker;

/**
 * 熔断器关闭状态
 */
public class CloseState extends AbstractState {

    @Override
    public boolean canPass(AbstractCircuitBreaker circuitBreaker) {
        // 关闭状态下所有请求都允许通过
        return true;
    }

    @Override
    public void switchState(AbstractCircuitBreaker circuitBreaker) {
        long period = Long.parseLong(circuitBreaker.getFailureRateFromCloseToOpen().split(CircuitBreaker.SPLITTER)[1]) * 1000;
        long now = System.currentTimeMillis();
        long threshold = Long.parseLong(circuitBreaker.getFailureRateFromCloseToOpen().split(CircuitBreaker.SPLITTER)[0]);
        if (now - stateTime > period) {
            if (failureNum.get() > threshold) {
                // 超过失败请求次数阈值切换到打开状态
                circuitBreaker.setState(new OpenState());
            }
            // 重置失败请求次数和时间戳
            failureNum.set(0);
            stateTime = System.currentTimeMillis();
        } else {
            // 未到一个周期但失败请求次数已超过阈值则直接切换到打开状态
            if (failureNum.get() > threshold) {
                circuitBreaker.setState(new OpenState());
            }
        }
    }

}
