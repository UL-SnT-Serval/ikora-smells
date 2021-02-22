package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ConditionalTestLogicVisitor;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.smells.*;

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
