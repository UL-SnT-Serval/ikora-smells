package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.Set;
import java.util.stream.Collectors;

public class HidingTestDataCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.GET);
        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));

        double rawValue = visitor.getNodes().size();
        double normalizedValue = visitor.getTotalVisited() > 0 ? rawValue / visitor.getTotalVisited() : 0.;

        return new SmellResult(SmellMetric.Type.HIDING_TEST_DATA, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .map(t -> computeMetric(t, configuration))
                .flatMap(r -> r.getNodes().stream())
                .filter(n -> n.getSourceFile() == file)
                .collect(Collectors.toSet());
    }
}
