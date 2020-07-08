package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.SleepCounterVisitor;

public class StinkySynchronizationSyndromeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        final SleepCounterVisitor visitor = new SleepCounterVisitor();
        visitor.visit(testCase, new PathMemory());

        final int sleepCalls = visitor.getSleepCalls();
        final int syncCalls = visitor.getSyncCalls();
        final double metric = (double)sleepCalls / (double)syncCalls;

        return new SmellResult(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, metric, visitor.getNodes());
    }
}
