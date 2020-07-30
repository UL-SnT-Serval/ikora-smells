package tech.ikora.smells.checks;

import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.ConditionalTestLogicVisitor;

import java.util.Set;

public class ConditionalTestLogicCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        ConditionalTestLogicVisitor visitor = new ConditionalTestLogicVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellResult(SmellMetric.Type.CONDITIONAL_TEST_LOGIC, visitor.getConditionsCount(), visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return false;
    }
}
