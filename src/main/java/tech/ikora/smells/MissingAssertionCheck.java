package tech.ikora.smells;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.visitors.MissingAssertionVisitor;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        MissingAssertionVisitor visitor = new MissingAssertionVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.MISSING_ASSERTION, visitor.getAssertionCount());
    }
}
