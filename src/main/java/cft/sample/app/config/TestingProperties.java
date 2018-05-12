package cft.sample.app.config;

import cft.sample.app.tester.DataGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "testing")
public class TestingProperties {

    /**
     * Marks that testing data pumping was enabled or not
     */
    private Boolean enabled;

    /**
     * The way testing data pump generates the testing data
     */
    private DataGenerator.MODE mode;
}
