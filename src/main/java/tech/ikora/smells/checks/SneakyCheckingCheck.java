package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.OneActionVisitor;

public class SneakyCheckingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        final int singleActionCount = visitor.getSingleActionCount();
        final int keywordsCounts = visitor.getKeywordsCount();
        final double metric = (double)singleActionCount / (double)keywordsCounts;

        return new SmellResult(SmellMetric.Type.SNEAKY_CHECKING, metric, visitor.getNodes());
    }
}
