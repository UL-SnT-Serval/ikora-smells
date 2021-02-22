package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.Keyword;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.smells.*;
import lu.uni.serval.ikora.smells.visitors.OneActionVisitor;

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
