package cft.sample.app.scheduler;

import cft.sample.app.config.AppProperties;
import cft.sample.app.scheduler.handlers.Handler;
import cft.sample.app.scheduler.workers.WorkerInadvance;
import cft.sample.app.scheduler.workers.WorkerSimpleRR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class Scheduler {

    @Autowired
    private Handler handler;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private WorkerInadvance workerInadvance;
    @Autowired
    private WorkerSimpleRR workerSimpleRR;

    @Scheduled(fixedRateString = "${common.workingPeriod}")
    private void worker() {
        appProperties.getOverflowPrevType().schedule(getByOverflowPrevType(appProperties.getOverflowPrevType()), handler);
    }

    private SchedulerVisitor getByOverflowPrevType(OverflowPreventionType overflowPrevType) {

        switch (overflowPrevType) {
            case INADVANCE:
                return workerInadvance;
            case SIMPLIFIED_RR:
                return workerSimpleRR;
            default:
                throw new NotImplementedException();
        }
    }
}
