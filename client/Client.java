package circuitbreaker.client;

import circuitbreaker.CircuitBreaker;
import circuitbreaker.StandaloneCircuitBreaker;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class Client {

    public static void main(String[] args) throws InterruptedException {
        int n = 200;
        CountDownLatch countDownLatch = new CountDownLatch(n);
        CircuitBreaker circuitBreaker = new StandaloneCircuitBreaker();
        for (int i = 0; i < n; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(new Random().nextInt(90) * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (circuitBreaker.canPass()) {
                        log.info("请求通过");
                        if (new Random().nextInt(2) == 1) {
                            throw new Exception("error");
                        }
                    } else {
                        log.info("请求不通过");
                    }
                } catch (Exception e) {
                    log.error("请求失败");
                    circuitBreaker.countFailure();
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        circuitBreaker.reset();
    }

}
