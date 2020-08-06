package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CloneVisitor;

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
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(action, nodes, Action.Type.REMOVE_USER_KEYWORD);
    }
}
