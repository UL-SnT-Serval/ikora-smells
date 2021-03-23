package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.OneActionVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

public class MiddleManCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.USER);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getSingleActionCount();
        double normalizedValue = rawValue / visitor.getKeywordsCount();

        return new SmellResult(SmellMetric.Type.MIDDLE_MAN, rawValue, normalizedValue, visitor.getNodes());
    }
}
