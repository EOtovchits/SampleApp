package cft.sample.app.config;

import cft.sample.app.scheduler.OverflowPreventionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "common")
public class AppProperties {

    /**
     * Number of threads to execute the work w/ item ids
     */
    private int threadsNum;

    /**
     * Jobs overflow prevention type
     */
    private OverflowPreventionType overflowPrevType;

    /**
     * Max. size for queue to hold the item ids of the same group id. Used when 'overflowPrevType' parameter above
     * is having the 'INADVANCE' value
     */
    private int queueMaxSize;

    /**
     * Max. size of 'composite' queue composed of shorter queues for different group ids. In the following processing
     * such 'composite' queue will be processes at one worker thread
     */
    private int rrThreshold;
}
