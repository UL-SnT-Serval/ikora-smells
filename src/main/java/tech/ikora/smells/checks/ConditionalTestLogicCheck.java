package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
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
        float metric = (float)visitor.getNodes().size() / (float)visitor.getConditionsCount();

        return new SmellResult(SmellMetric.Type.CONDITIONAL_TEST_LOGIC, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP, Edit.Type.CHANGE_STEP);
    }
}
