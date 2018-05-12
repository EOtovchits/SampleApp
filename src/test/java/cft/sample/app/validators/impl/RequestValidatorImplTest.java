package cft.sample.app.validators.impl;

import cft.sample.app.exceptions.SampleAppException;
import cft.sample.app.validators.RequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static cft.sample.app.exceptions.ErrCodes.ORDERING_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RequestValidatorImpl.class})
@EnableConfigurationProperties
@PropertySource("classpath:application-test.yml")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class RequestValidatorImplTest {

    @Autowired
    RequestValidator requestValidator;

    @BeforeEach
    void setUp() {
        requestValidator.validateIncomingIdOrder(1L, 1L);
    }

    /**
     * Test that simple checking is working
     */
    @Test
    void validateIncomingIdOrder_simpleChecking() {

        Boolean res = requestValidator.validateIncomingIdOrder(1L, 3L);
        assertTrue(res);
    }

    /**
     * Test that larger value added to key w/o any issues
     */
    @Test
    void validateIncomingIdOrder_simpleCheckingMultiple() {
        Boolean res = requestValidator.validateIncomingIdOrder(1L, 3L);
        assertTrue(res);

        res = requestValidator.validateIncomingIdOrder(1L, 4L);
        assertTrue(res);
    }

    /**
     * Test the case when less value is used to perform the validation
     */
    @Test
    void validateIncomingOrder_wrongOrdering() {
        Boolean res = requestValidator.validateIncomingIdOrder(1L, 3L);
        assertTrue(res);

        Throwable ex = assertThrows(SampleAppException.class,
                () -> requestValidator.validateIncomingIdOrder(1L, 1L)
        );

        assertEquals(((SampleAppException) ex).getErrMess(), ORDERING_ERROR.getMess());
    }
}