package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.ConditionalTestLogicVisitor;

public class ConditionalTestLogicCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        ConditionalTestLogicVisitor visitor = new ConditionalTestLogicVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.CONDITIONAL_TEST_LOGIC, visitor.getConditionsCount());
    }
}
