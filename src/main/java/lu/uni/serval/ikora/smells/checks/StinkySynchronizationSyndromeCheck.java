package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.SleepCounterVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;

public class StinkySynchronizationSyndromeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final SleepCounterVisitor visitor = new SleepCounterVisitor();
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getSleepCalls();
        double normalizedValue = rawValue / visitor.getSyncCalls();

        return new SmellResult(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, rawValue, normalizedValue, visitor.getNodes());
    }
}
