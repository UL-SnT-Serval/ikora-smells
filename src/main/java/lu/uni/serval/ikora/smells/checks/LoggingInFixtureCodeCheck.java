package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;
import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LoggingInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        Set<SourceNode> nodes = Collections.emptySet();
        double rawValue = Double.NaN;
        double normalizedValud = Double.NaN;

        if(testCase.getSetup().isPresent() || testCase.getTearDown().isPresent()){
            int statements = testCase.getSetup().map(KeywordStatistics::getStatementCount).orElse(1) - 1
                    + testCase.getTearDown().map(KeywordStatistics::getStatementCount).orElse(1) - 1;

            nodes = getFixtureLoggingNodes(testCase);
            rawValue = nodes.size();
            normalizedValud = rawValue / statements;
        }

        return new SmellResult(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, rawValue, normalizedValud, nodes);
    }

    @Override
    public List<Node> collectInstances(SourceFile file) {
        return null;
    }

    private Set<SourceNode> getFixtureLoggingNodes(TestCase testCase){
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.LOG);

        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));
        testCase.getTearDown().ifPresent(s -> visitor.visit(s, new PathMemory()));

        return visitor.getNodes();
    }
}
