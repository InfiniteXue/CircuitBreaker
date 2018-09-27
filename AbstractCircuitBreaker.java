package circuitbreaker;

import circuitbreaker.state.CloseState;
import circuitbreaker.state.State;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 熔断器抽象类
 */
@Slf4j
@Getter
@NoArgsConstructor
public abstract class AbstractCircuitBreaker implements CircuitBreaker {

    /**
     * 熔断器当前状态
     */
    private volatile State state = new CloseState();

    /**
     * M/N---N秒内失败请求达到M次时，熔断器状态从关闭切换到打开
     */
    private String failureRateFromCloseToOpen = "10/20";

    /**
     * 熔断器状态从打开切换到半开的等待时间(秒)
     */
    private long waitTimeFromOpenToHalfOpen = 10;

    /**
     * M/N---N秒内试探M次请求(半开状态时)
     */
    private String passRateWhenHalfOpen = "5/10";

    /**
     * 半开状态的试探期间内，若失败请求次数达到该阈值，则切换到打开状态，否则切换到关闭状态
     */
    private int failureThresholdWhenHalfOpen = 1;

    public AbstractCircuitBreaker(String failureRateFromCloseToOpen, long waitTimeFromOpenToHalfOpen, String passRateWhenHalfOpen, int failureThresholdWhenHalfOpen) {
        this.failureRateFromCloseToOpen = failureRateFromCloseToOpen;
        this.waitTimeFromOpenToHalfOpen = waitTimeFromOpenToHalfOpen;
        this.passRateWhenHalfOpen = passRateWhenHalfOpen;
        this.failureThresholdWhenHalfOpen = failureThresholdWhenHalfOpen;
    }

    public void setState(State state) {
        if (this.state.getClass().getSimpleName().equals(state.getClass().getSimpleName())) {
            return;
        }
        synchronized (this) {
            if (this.state.getClass().getSimpleName().equals(state.getClass().getSimpleName())) {
                return;
            }
            log.info("熔断器状态切换:{}-->{}", this.state.getClass().getSimpleName(), state.getClass().getSimpleName());
            this.state = state;
        }
    }

}
