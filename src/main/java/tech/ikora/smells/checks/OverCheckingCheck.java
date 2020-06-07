package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.ActionCounterVisitor;

public class OverCheckingCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        ActionCounterVisitor visitor = new ActionCounterVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getActionCount() / (double)visitor.getTotalCallCount();

        return new SmellMetric(SmellMetric.Type.OVER_CHECKING, metric);
    }
}
