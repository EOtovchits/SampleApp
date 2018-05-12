package cft.sample.app.tester;

import cft.sample.app.config.ServerProperties;
import cft.sample.app.config.TestingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Generates and sends pairs of group id and item id
 */

@Slf4j
@Component
public class RequestsPump {

    private static final String URI_TEMPLATE = "http://localhost:%d/next/group/%d/item/%d";

    @Autowired
    ServerProperties serverProperties;
    @Autowired
    TestingProperties testingProperties;

    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRateString = "${testing.pumpingPeriod}")
    private void runRequests() {

        if (Boolean.FALSE.equals(testingProperties.getEnabled())) {
            log.info("Testing pumping was disabled.");
            return;
        }

        GroupItemPair pair = DataGenerator.get();

        final String URI = String.format(URI_TEMPLATE, serverProperties.getPort(), pair.getGroupId(), pair.getItemId());

        String result = restTemplate.getForObject(URI, String.class);

        log.info("Got some result: [{}]", result);
    }
}
