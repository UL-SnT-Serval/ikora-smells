package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.OneActionVisitor;

public class MiddleManCHeck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.ACTION);
        visitor.visit(testCase, new PathMemory());

        final int singleActionCount = visitor.getSingleActionCount();
        final int keywordsCounts = visitor.getKeywordsCount();
        final double metric = (double)singleActionCount / (double)keywordsCounts;

        return new SmellMetric(SmellMetric.Type.MIDDLE_MAN, metric);
    }
}
