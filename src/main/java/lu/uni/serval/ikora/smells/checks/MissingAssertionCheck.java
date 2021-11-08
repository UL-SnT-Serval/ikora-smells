package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double normalized = visitor.getNodes().isEmpty() ? 1. : 0.;
        final Set<SourceNode> nodes = visitor.getNodes().isEmpty() ? Collections.singleton(testCase) : Collections.emptySet();

        return new SmellResult(SmellMetric.Type.MISSING_ASSERTION, normalized, normalized, nodes);
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .map(t -> computeMetric(t, configuration))
                .flatMap(r -> r.getNodes().stream())
                .collect(Collectors.toSet());
    }
}
