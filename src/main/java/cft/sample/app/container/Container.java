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

/**
 * Here we keep the queues w/ item ids. Each queue (an ArrayList actually)
 * keeps the item ids of some group id, so the number of queues is equal to
 * the number of group ids. The item ids come here already in some order.
 */

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

    /**
     * Some optional validation. When 'common.overflowPrevType' param is INADVANCE
     * we throw away incoming item id, if its amount for some group id is more than
     * 'common.queueMaxSize' parameter value.
     *
     * @param groupId - group id
     * @param itemId  - incoming item id
     * @throws SampleAppException - throw this when queue is full for this group id
     */
    public void validateQueueCapacity(Long groupId, Long itemId) throws SampleAppException {
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

    /**
     * Add new item id the it's queue
     * @param groupId - group id
     * @param itemId - item id
     * @return - so far always return true
     */
    public synchronized Boolean addItem(Long groupId, Long itemId) {

        List<Long> items = quesues.get(groupId);

        if (items == null) {
            items = new ArrayList<>();
        }

        items.add(itemId);

        quesues.put(groupId, items);

        return true;
    }

    /**
     * Return and delete the item ids queue related to some group id
     * @param groupId - group id
     * @return - list of item ids related to group id
     */
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

    /**
     * Return and delete the list of pairs (group id : item id) related to some group id
     * @param groupId - group id
     * @return - list of pairs (group id : item id) related to group id
     */
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

    /**
     * Returns the sorted list of pairs (group id : item ids amount), representing the current
     * state of all queues
     * @return - sorted list of pairs (group id : item ids amount)
     */
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
