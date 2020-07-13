package tech.ikora.smells.checks;

import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.CloneVisitor;

import java.util.Set;

public class TestClonesCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        CloneVisitor visitor = new CloneVisitor(detector.getClones());
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getCloneCount() / (double)visitor.getTotalKeywordsCounter();

        return new SmellResult(SmellMetric.Type.TEST_CLONES, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes) {
        return false;
    }
}
