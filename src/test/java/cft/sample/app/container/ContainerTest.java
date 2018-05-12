package cft.sample.app.container;

import cft.sample.app.config.AppProperties;
import cft.sample.app.exceptions.SampleAppException;
import cft.sample.app.scheduler.OverflowPreventionType;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static cft.sample.app.exceptions.ErrCodes.QUEUE_MAX_SIZE_EXCEEDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@Slf4j
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {Container.class, AppProperties.class})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@EnableConfigurationProperties
@PropertySource("classpath:application-test.yml")
class ContainerTest {

    @MockBean
    private AppProperties appProperties;

    @Autowired
    private Container container;

    final int MAX_QUEUE_SIZE = 4;

    @BeforeEach
    void setUp() {
        given(appProperties.getOverflowPrevType()).willReturn(OverflowPreventionType.INADVANCE);
        given(appProperties.getQueueMaxSize()).willReturn(MAX_QUEUE_SIZE);
    }

    /**
     * Test the following case:
     * 1. We're in INADVANCE mode
     * 2. Trying to insert more items to the queue than MAX_QUEUE_SIZE
     */
    @Test
    void validateQueueCapacity_checkOverflowWithNotInAdvanceMode() {

        for (Long ii = 0L; ii < MAX_QUEUE_SIZE; ii++) {
            container.addItem(1L, ii);
        }

        Throwable ex = assertThrows(SampleAppException.class,
                () -> container.validateQueueCapacity(1L, 7L)
        );

        assertEquals(((SampleAppException) ex).getErrMess(), QUEUE_MAX_SIZE_EXCEEDED.getMess());
    }

    /**
     * Test that kept items snapshot is correct
     */
    @Test
    void getSnapshot() {

        container.addItem(6L, 15L);
        container.addItem(6L, 16L);
        container.addItem(6L, 17L);
        container.addItem(6L, 18L);

        container.addItem(10L, 0L);
        container.addItem(10L, 1L);

        container.addItem(3L, 5L);
        container.addItem(3L, 6L);
        container.addItem(3L, 7L);

        List<Pair<Long, Long>> snapshot = container.getSnapshot();

        /// The total size
        assertEquals(snapshot.size(), 3);

        /// The correct order
        assertEquals(snapshot.get(0).getKey().longValue(), 10);
        assertEquals(snapshot.get(1).getKey().longValue(), 3);
        assertEquals(snapshot.get(2).getKey().longValue(), 6);

        /// The correct amount of item ids
        assertEquals(snapshot.get(0).getValue().longValue(), 2);
        assertEquals(snapshot.get(1).getValue().longValue(), 3);
        assertEquals(snapshot.get(2).getValue().longValue(), 4);
    }
}