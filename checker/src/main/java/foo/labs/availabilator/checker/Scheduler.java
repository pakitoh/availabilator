package foo.labs.availabilator.checker;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private final int rate;
    private final int poolSize;
    private final int initialDelay;
    private ScheduledExecutorService scheduler;

    public Scheduler(Map<String, String> ctx) {
        this.rate = Integer.parseInt(ctx.get(ContextBuilder.POOLING_RATE));
        this.initialDelay = Integer.parseInt(ctx.get(ContextBuilder.INITIAL_DELAY));
        this.poolSize = Integer.parseInt(ctx.get(ContextBuilder.POOL_SIZE));
        this.scheduler = Executors.newScheduledThreadPool(poolSize);
    }

    public ScheduledFuture schedule(Runnable task) {
        return scheduler.scheduleAtFixedRate(
                task,
                initialDelay,
                rate,
                TimeUnit.MILLISECONDS);
    }
}
