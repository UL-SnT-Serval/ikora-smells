package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.smells.visitors.CloneVisitor;

import java.util.Set;

public class TestClonesCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CloneVisitor visitor = new CloneVisitor(configuration.getClones());
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getCloneCount() / (double)visitor.getTotalKeywordsCount();

        return new SmellResult(SmellMetric.Type.TEST_CLONES, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_USER_KEYWORD);
    }
}
