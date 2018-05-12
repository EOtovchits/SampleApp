package cft.sample.app.validators.impl;

import cft.sample.app.exceptions.SampleAppException;
import cft.sample.app.validators.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static cft.sample.app.exceptions.ErrCodes.ORDERING_ERROR;

/**
 * Implementation of incoming information validation
 */

@Slf4j
@Component
public class RequestValidatorImpl implements RequestValidator {

    private Map<Long, Long> maxValuesMap;

    @PostConstruct
    private void init(){
        maxValuesMap = new HashMap<>();
    }

    /**
     * Validates the order of new incoming item ids for some group id. If new item id is less than one
     * already cached - exception is raised
     *
     * @param groupId - group id
     * @param itemId  - item id
     * @return - so far returns true if no issues found, otherwise exception thrown
     * @throws SampleAppException - raised when new item id for some particular group id is less than cached one
     */
    @Override
    public Boolean validateIncomingIdOrder(Long groupId, Long itemId) throws SampleAppException {

        Long savedValue = maxValuesMap.get(groupId);

        if (savedValue != null && savedValue >= itemId){
            log.info("groupId=[{}], savedValue=[{}], itemId=[{}]", groupId, savedValue, itemId);
            throw new SampleAppException(ORDERING_ERROR, groupId, itemId);
        }

        maxValuesMap.put(groupId, itemId);

        return true;
    }
}
