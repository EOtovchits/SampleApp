package cft.sample.app.scheduler.workers;

import cft.sample.app.config.AppProperties;
import cft.sample.app.container.Container;
import cft.sample.app.scheduler.SchedulerVisitor;
import cft.sample.app.scheduler.handlers.Handler;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Worker class to implement the 'in advance' overflow prevention logic
 */

@Slf4j
@Component
public class WorkerInadvance extends WorkerAbstract implements SchedulerVisitor {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private Container container;

    public WorkerInadvance() {
    }

    @PostConstruct
    private void init() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(appProperties.getThreadsNum());
    }

    @Override
    public Boolean handleInAdvance(Handler handler) {

        work(handler);
        return true;
    }

    @Override
    public Boolean handleSimpleRoundRobin(Handler handler) {
        log.error("We're in 'In advance' worker");
        throw new NotImplementedException();
    }

    /**
     * Iterates through container snapshot, extracts the list of item ids for each
     * group id and passes all this staff as-is to the handler for the following processing
     *
     * @param handler - handles the pair group id : item id
     * @return - so far always returns true
     */
    @Override
    protected Boolean work(Handler handler) {

        List<Pair<Long, Long>> snapshot = container.getSnapshot();

        snapshot.stream().forEach(pair -> {
            List<Long> items = container.getAndCleanByGroupId(pair.getKey());
            threadPoolExecutor.execute(() -> {
                items.stream().forEach(item ->
                        handler.processItem(pair.getKey(), item)
                );
            });
        });

        return true;
    }
}
