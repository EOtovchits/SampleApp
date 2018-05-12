package cft.sample.app.tester;

import cft.sample.app.config.TestingProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A testing data (group and item id) generator
 */

@Component
public class DataGenerator {

    /**
     * Modes used to generate the testing data
     */
    public enum MODE {

        /**
         * A single group id and linearly increasing row or item ids
         */
        LINEAR {
            @Override
            public <E> E getPair(ModeVisitor<E> visitor) {
                return visitor.getLinear();
            }
        },

        /**
         * Some group ids. Linearly increasing row of item ids assigned to some group id in some
         * random order
         */
        RANDOM {
            @Override
            public <E> E getPair(ModeVisitor<E> visitor) {
                return visitor.getRandom();
            }
        },

        /**
         * Return everything you wish
         */
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