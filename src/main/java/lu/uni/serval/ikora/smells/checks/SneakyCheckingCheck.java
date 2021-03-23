package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.OneActionVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.TestCase;

public class SneakyCheckingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getSingleActionCount();
        double normalizedValue = rawValue / visitor.getKeywordsCount();

        return new SmellResult(SmellMetric.Type.SNEAKY_CHECKING, rawValue, normalizedValue, visitor.getNodes());
    }
}
