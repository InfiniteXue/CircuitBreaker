package circuitbreaker;

import circuitbreaker.state.CloseState;

/**
 * 单机熔断器
 */
public class StandaloneCircuitBreaker extends AbstractCircuitBreaker {

    @Override
    public void reset() {
        setState(new CloseState());
    }

    @Override
    public boolean canPass() {
        return getState().canPass(this);
    }

    @Override
    public void countFailure() {
        getState().countFailure(this);
    }

}
