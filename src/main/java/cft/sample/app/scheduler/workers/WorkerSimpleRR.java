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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Worker class to implement the 'simple round robin' overflow prevention logic
 */

@Slf4j
@Component
public class WorkerSimpleRR extends WorkerAbstract implements SchedulerVisitor {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private Container container;

    public WorkerSimpleRR() {
    }

    @PostConstruct
    private void init() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(appProperties.getThreadsNum());
    }

    @Override
    public Boolean handleInAdvance(Handler handler) {
        log.error("We're in in 'Simple round robin' worker");
        throw new NotImplementedException();
    }

    @Override
    public Boolean handleSimpleRoundRobin(Handler handler) {
        work(handler);
        return true;
    }

    /**
     * Iterates through container snapshot and extracts the list of item ids for each
     * group id. After this tries to compose the small sets of item ids into a single
     * list. If success, sends it along w/ large sets to the handler to the following
     * processing.
     *
     * @param handler - handles the pair group id : item id
     * @return - so far always returns true
     */
    @Override
    protected Boolean work(Handler handler) {
        List<Pair<Long, Long>> snapshot = container.getSnapshot();

        List<Pair<Long, Long>> temp = new ArrayList<>();
        for (Pair<Long, Long> runner : snapshot) {

            List<Pair<Long, Long>> values = container.getAsPairAndCleanByGroupId(runner.getKey());

            temp.addAll(values);

            if (temp.size() >= appProperties.getRrThreshold()) {

                threadPoolExecutor.execute(() -> new ArrayList<>(temp).stream().forEach(pair ->
                        handler.processItem(pair.getKey(), pair.getValue())
                ));

                temp.clear();
            }
        }

        return true;
    }
}
