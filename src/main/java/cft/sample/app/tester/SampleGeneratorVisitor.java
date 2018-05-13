package cft.sample.app.tester;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of testing data generation strategies (look at cft.sample.app.tester.DataGenerator.MODE)
 */

public final class SampleGeneratorVisitor implements DataGenerator.MODE.ModeVisitor<GroupItemPair> {

    private Long counter = 0L;

    @Override
    public GroupItemPair getLinear() {
        return new GroupItemPair(1L, ++counter);
    }

    @Override
    public GroupItemPair getRandom() {
        return new GroupItemPair(ThreadLocalRandom.current().nextLong(0, 10),
                ++counter);
    }

    @Override
    public GroupItemPair getManual() {
        /// Implement some another custom scenario
        throw new NotImplementedException();
    }
}
