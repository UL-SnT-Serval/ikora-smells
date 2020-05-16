package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.ResourceOptimismVisitor;

public class StinkySynchronizationSyndromeCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        ResourceOptimismVisitor visitor = new ResourceOptimismVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, visitor.getNumberSleepCalls());
    }
}
