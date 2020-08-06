package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Set;

public class OverCheckingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getNodes().size() / (double)visitor.getTotalVisited();

        return new SmellResult(SmellMetric.Type.OVER_CHECKING, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
    }
}
