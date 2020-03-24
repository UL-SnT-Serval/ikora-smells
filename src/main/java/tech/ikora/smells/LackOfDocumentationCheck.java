package tech.ikora.smells;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.visitors.LackOfDocumentationVisitor;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.LACK_OF_DOCUMENTATION, visitor.getDocumentedKeywordProportion());
    }
}
