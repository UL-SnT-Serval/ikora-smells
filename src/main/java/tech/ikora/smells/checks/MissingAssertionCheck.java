package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double metric = visitor.getNodes().isEmpty() ? 0. : 1.;

        return new SmellResult(SmellMetric.Type.MISSING_ASSERTION, metric, visitor.getNodes());
    }
}
