package cft.sample.app.scheduler;

import cft.sample.app.scheduler.handlers.Handler;

/**
 * Interface used to handle overflow prevention strategies
 */

public interface SchedulerVisitor {

    Boolean handleInAdvance(Handler handler);

    Boolean handleSimpleRoundRobin(Handler handler);
}
