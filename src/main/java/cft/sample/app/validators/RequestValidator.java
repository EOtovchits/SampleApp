package cft.sample.app.validators;

import cft.sample.app.exceptions.SampleAppException;

/**
 * Validates incoming information
 */

public interface RequestValidator {

    Boolean validateIncomingIdOrder(Long groupId, Long itemId) throws SampleAppException;
}
