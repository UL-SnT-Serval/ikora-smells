package tech.ikora.smells.checks;

import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Set;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double metric = visitor.getNodes().isEmpty() ? 1. : 0.;

        return new SmellResult(SmellMetric.Type.MISSING_ASSERTION, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return false;
    }
}
