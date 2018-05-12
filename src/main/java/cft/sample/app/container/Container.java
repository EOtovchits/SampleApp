package cft.sample.app.container;

import cft.sample.app.config.AppProperties;
import cft.sample.app.exceptions.SampleAppException;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static cft.sample.app.exceptions.ErrCodes.QUEUE_MAX_SIZE_EXCEEDED;
import static cft.sample.app.scheduler.OverflowPreventionType.INADVANCE;

@Slf4j
@Component
public class Container {

    @Autowired
    AppProperties appProperties;

    private Map<Long, List<Long>> quesues;

    @PostConstruct
    private void init() {
        quesues = new HashMap<>();
    }

    public void validateQueueCapacity(Long groupId, Long itemId) {
        if (appProperties.getOverflowPrevType() != INADVANCE) {
            return;
        }

        List<Long> savedItems;

        synchronized (this) {
            savedItems = quesues.get(groupId);
        }

        if (savedItems != null && (savedItems.size() + 1) > appProperties.getQueueMaxSize()) {
            throw new SampleAppException(QUEUE_MAX_SIZE_EXCEEDED, groupId, itemId);
        }
    }

    public synchronized Boolean addItem(Long groupId, Long itemId) {

        List<Long> items = quesues.get(groupId);

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(itemId);

        quesues.put(groupId, items);

        return true;
    }

    public List<Long> getAndCleanByGroupId(Long groupId) {

        List<Long> items;

        synchronized (this) {
            items = quesues.get(groupId);
            quesues.put(groupId, null);
        }

        if (items == null) {
            return Collections.emptyList();
        } else {
            return items;
        }
    }

    public List<Pair<Long, Long>> getAsPairAndCleanByGroupId(Long groupId) {

        List<Long> items;

        synchronized (this) {
            items = quesues.get(groupId);
            quesues.put(groupId, null);
        }

        if (items == null) {
            return Collections.emptyList();
        } else {
            return items.stream().map(item -> new Pair<>(groupId, item)).collect(Collectors.toList());
        }
    }

    public List<Pair<Long, Long>> getSnapshot() {

        List<Pair<Long, Long>> pairs;

        synchronized (this) {
            pairs = new ArrayList<>(quesues.size());
            quesues.forEach((key, value) -> pairs.add(new Pair<Long, Long>(key, Long.valueOf(value.size()))));
        }

        pairs.sort(Comparator.comparing(Pair::getValue));
        return pairs;
    }
}
