package cft.sample.app.tester;

import cft.sample.app.config.TestingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataGenerator {

    public enum MODE {
        LINEAR {
            @Override
            public <E> E getPair(ModeVisitor<E> visitor) {
                return visitor.getLinear();
            }
        },

        RANDOM {
            @Override
            public <E> E getPair(ModeVisitor<E> visitor) {
                return visitor.getRandom();
            }
        },

        MANUAL {
            @Override
            <E> E getPair(ModeVisitor<E> visitor) {
                return visitor.getManual();
            }
        };

        abstract <E> E getPair(ModeVisitor<E> visitor);

        interface ModeVisitor<E> {
            E getLinear();

            E getRandom();

            E getManual();
        }
    }

    private static TestingProperties testingProperties;
    private static SampleVisitor sampleVisitor = new SampleVisitor();

    @Autowired
    public void setTestingProperties(TestingProperties testingProperties) {
        DataGenerator.testingProperties = testingProperties;
    }

    public static GroupItemPair get() {
        return testingProperties.getMode().getPair(sampleVisitor);
    }
}

final class SampleVisitor implements DataGenerator.MODE.ModeVisitor<GroupItemPair> {

    private Long counter = 0L;

    @Override
    public GroupItemPair getLinear() {
        return new GroupItemPair(1L, ++counter);
    }

    @Override
    public GroupItemPair getRandom() {
        return new GroupItemPair(ThreadLocalRandom.current().nextLong(0, 3),
                ++counter);
    }

    @Override
    public GroupItemPair getManual() {
        return null;
    }
}
