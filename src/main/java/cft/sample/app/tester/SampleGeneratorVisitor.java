package cft.sample.app.tester;

import java.util.concurrent.ThreadLocalRandom;

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
        return new GroupItemPair(1L, 1234L);
    }
}
