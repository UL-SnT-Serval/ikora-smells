package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.OneActionVisitor;

import java.util.Set;

public class MiddleManCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.USER);
        visitor.visit(testCase, new PathMemory());

        final int singleActionCount = visitor.getSingleActionCount();
        final int keywordsCounts = visitor.getKeywordsCount();
        final double metric = (double)singleActionCount / (double)keywordsCounts;

        return new SmellResult(SmellMetric.Type.MIDDLE_MAN, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(change, nodes, Action.Type.REMOVE_USER_KEYWORD);
    }
}
