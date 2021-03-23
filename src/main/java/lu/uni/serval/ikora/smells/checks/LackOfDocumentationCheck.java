package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.LackOfDocumentationVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        final double rawValue = visitor.getDocumentedKeyword();
        final double normalizedValue = rawValue / visitor.getTotalKeywords();

        return new SmellResult(SmellMetric.Type.LACK_OF_DOCUMENTATION, rawValue, normalizedValue, visitor.getNodes());
    }
}
