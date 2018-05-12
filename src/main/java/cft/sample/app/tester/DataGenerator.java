package cft.sample.app.tester;

import cft.sample.app.config.TestingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private static SampleGeneratorVisitor sampleVisitor = new SampleGeneratorVisitor();

    @Autowired
    public void setTestingProperties(TestingProperties testingProperties) {
        DataGenerator.testingProperties = testingProperties;
    }

    public static GroupItemPair get() {
        return testingProperties.getMode().getPair(sampleVisitor);
    }
}