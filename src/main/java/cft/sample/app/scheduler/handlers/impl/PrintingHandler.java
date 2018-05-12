package cft.sample.app.scheduler.handlers.impl;

import cft.sample.app.scheduler.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * This particular handler just logs the pair (group id and item id)
 */

@Slf4j
@Component
public class PrintingHandler implements Handler {
    @Override
    public void processItem(Long groupId, Long itemId) {
        log.info("Processing pair: groupId = [{}], itemId = [{}]", groupId, itemId);
    }
}
