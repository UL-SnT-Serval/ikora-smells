package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.LackOfDocumentationVisitor;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = visitor.getDocumentedKeywordProportion();

        return new SmellMetric(SmellMetric.Type.LACK_OF_DOCUMENTATION, metric);
    }
}
