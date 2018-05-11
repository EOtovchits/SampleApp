package cft.sample.app.scheduler.workers;

import cft.sample.app.config.AppProperties;
import cft.sample.app.container.Container;
import cft.sample.app.scheduler.handlers.Handler;
import cft.sample.app.scheduler.SchedulerVisitor;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class WorkerInadvance extends WorkerAbstract implements SchedulerVisitor {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private Container container;

    WorkerInadvance() {
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
    public Boolean handleSimpleRaundRobin(Handler handler) {
        log.error("We're in 'In advance' worker");
        throw new NotImplementedException();
    }

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
