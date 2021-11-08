package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.HashSet;
import java.util.Set;

public class OverCheckingCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getNodes().size();
        double normalizedValue = rawValue / visitor.getTotalVisited();

        return new SmellResult(SmellMetric.Type.OVER_CHECKING, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = new HashSet<>();

        for(TestCase testCase: file.getTestCases()){
            final SmellResult result = computeMetric(testCase, configuration);

            if(result.getNormalizedValue() > configuration.getAssertionDensityThreshold()){
                nodes.add(testCase);
            }
        }

        return nodes;
    }
}
