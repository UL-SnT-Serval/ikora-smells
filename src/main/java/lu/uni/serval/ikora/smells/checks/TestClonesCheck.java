package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CloneVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.List;

public class TestClonesCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CloneVisitor visitor = new CloneVisitor(configuration.getClones());
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getCloneCount();
        double normalizedValue = rawValue / visitor.getTotalKeywordsCount();

        return new SmellResult(SmellMetric.Type.TEST_CLONES, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public List<Node> collectInstances(SourceFile file) {
        return null;
    }
}
