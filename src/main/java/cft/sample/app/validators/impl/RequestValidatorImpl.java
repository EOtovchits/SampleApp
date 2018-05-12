package cft.sample.app.validators.impl;

import cft.sample.app.exceptions.SampleAppException;
import cft.sample.app.validators.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static cft.sample.app.exceptions.ErrCodes.ORDERING_ERROR;

@Slf4j
@Component
public class RequestValidatorImpl implements RequestValidator {

    private Map<Long, Long> maxValuesMap;

    @PostConstruct
    private void init(){
        maxValuesMap = new HashMap<>();
    }

    @Override
    public Boolean validateIncomingIdOrder(Long groupId, Long itemId) {

        Long savedValue = maxValuesMap.get(groupId);

        if (savedValue != null && savedValue >= itemId){
            log.info("groupId=[{}], savedValue=[{}], itemId=[{}]", groupId, savedValue, itemId);
            throw new SampleAppException(ORDERING_ERROR, groupId, itemId);
        }

        maxValuesMap.put(groupId, itemId);

        return true;
    }
}
