package cft.sample.app.scheduler.workers;

import cft.sample.app.scheduler.handlers.Handler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * An abstract class which descendants will execute needed works over pairs of group and item ids
 */

public abstract class WorkerAbstract {

    protected ThreadPoolExecutor threadPoolExecutor;

    protected WorkerAbstract() {
    }

    abstract protected Boolean work(Handler handler);
}
