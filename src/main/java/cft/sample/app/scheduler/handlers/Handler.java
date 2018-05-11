package cft.sample.app.scheduler.handlers;

public interface Handler {

    void processItem(Long groupId, Long itemId);
}
