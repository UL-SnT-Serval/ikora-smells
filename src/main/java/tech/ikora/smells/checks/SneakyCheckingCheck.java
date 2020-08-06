package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.OneActionVisitor;

import java.util.Set;

public class SneakyCheckingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        final int singleActionCount = visitor.getSingleActionCount();
        final int keywordsCounts = visitor.getKeywordsCount();
        final double metric = (double)singleActionCount / (double)keywordsCounts;

        return new SmellResult(SmellMetric.Type.SNEAKY_CHECKING, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_NODE);
    }
}
