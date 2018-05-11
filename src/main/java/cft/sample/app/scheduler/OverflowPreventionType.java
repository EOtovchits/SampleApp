package cft.sample.app.scheduler;

import cft.sample.app.scheduler.handlers.Handler;

public enum OverflowPreventionType {

    INADVANCE {
        @Override
        void schedule(SchedulerVisitor visitor, Handler handler) {
            visitor.handleInAdvance(handler);
        }
    },

    SIMPLIFIED_RR {
        @Override
        void schedule(SchedulerVisitor visitor, Handler handler) {
            visitor.handleSimpleRaundRobin(handler);
        }
    };

    abstract void schedule(SchedulerVisitor visitor, Handler handler);
}
