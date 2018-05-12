package cft.sample.app.scheduler.handlers;

/**
 * Handler takes responsibility about final processing of pair group id and item id
 */

public interface Handler {

    void processItem(Long groupId, Long itemId);
}
