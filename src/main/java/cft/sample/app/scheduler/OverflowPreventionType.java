package cft.sample.app.scheduler;

import cft.sample.app.scheduler.handlers.Handler;

/**
 * Enum representing strategies to prevent group and item id pairs provessing
 * overflow.
 */

public enum OverflowPreventionType {

    /**
     * Main idea is the following: if amount of saved item ids for particular
     * group id exceeds some value (common.queueMaxSize parameter) any new item ids
     * will be discarded until the already saved item ids are processed by some
     * handler
     */
    INADVANCE {
        @Override
        void schedule(SchedulerVisitor visitor, Handler handler) {
            visitor.handleInAdvance(handler);
        }
    },

    /**
     * Main idea is next here: any incoming item ids are kept w/o any limitations and
     * during processing we group or compose the small sets of item ids into a larger
     * set and process them at once. Thus we eliminate the need to spent resources on
     * group ids w/ small sets of item ids
     */
    SIMPLIFIED_RR {
        @Override
        void schedule(SchedulerVisitor visitor, Handler handler) {
            visitor.handleSimpleRoundRobin(handler);
        }
    };

    abstract void schedule(SchedulerVisitor visitor, Handler handler);
}
