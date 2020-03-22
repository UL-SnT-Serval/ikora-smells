package tech.ikora.smells;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.visitors.ResourceOptimismVisitor;

public class ResourceOptimismCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        ResourceOptimismVisitor visitor = new ResourceOptimismVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.RESOURCE_OPTIMISM, visitor.getNumberSleepCalls());
    }
}
