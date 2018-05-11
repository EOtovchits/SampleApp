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

@Slf4j
@Component
public class WorkerSimpleRR extends WorkerAbstract implements SchedulerVisitor {

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private Container container;

    WorkerSimpleRR() {
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
    public Boolean handleSimpleRaundRobin(Handler handler) {
        work(handler);
        return true;
    }

    @Override
    protected Boolean work(Handler handler) {
        List<Pair<Long, Long>> snapshot = container.getSnapshot();

        List<Pair<Long, Long>> temp = new ArrayList<>();
        for (Pair<Long, Long> runner : snapshot) {

            List<Pair<Long, Long>> values = container.getAsPairAndCleanByGroupId(runner.getKey());

            temp.addAll(values);

            if (temp.size() >= appProperties.getRrThreshold()) {

                /// Try to simulate runnables overflow
                threadPoolExecutor.execute(() -> new ArrayList<>(temp).stream().forEach(pair ->
                        handler.processItem(pair.getKey(), pair.getValue())
                ));

                temp.clear();
            }
        }

        return true;
    }
}
