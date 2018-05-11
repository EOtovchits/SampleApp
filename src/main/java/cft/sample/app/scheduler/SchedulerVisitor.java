package cft.sample.app.scheduler;

import cft.sample.app.scheduler.handlers.Handler;

public interface SchedulerVisitor {

    Boolean handleInAdvance(Handler handler);

    Boolean handleSimpleRaundRobin(Handler handler);
}
