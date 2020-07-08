package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.LackOfDocumentationVisitor;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        final int documented = visitor.getDocumentedKeyword();
        final int total = visitor.getTotalKeywords();
        final double metric = (double)documented / (double)total;

        return new SmellResult(SmellMetric.Type.LACK_OF_DOCUMENTATION, metric, visitor.getNodes());
    }
}
