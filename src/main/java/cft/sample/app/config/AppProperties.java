package cft.sample.app.config;

import cft.sample.app.scheduler.OverflowPreventionType;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "common")
public class AppProperties {

    private int groupsNum;

    private int threadsNum;

    private OverflowPreventionType overflowPrevType;

    private int queueMaxSize;

    private int rrThreshold;
}
