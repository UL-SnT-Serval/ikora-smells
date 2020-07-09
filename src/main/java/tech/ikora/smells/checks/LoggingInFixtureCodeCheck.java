package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Set;

public class LoggingInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        int statements = testCase.getSetup().map(KeywordStatistics::getStatementCount).orElse(0)
                + testCase.getTearDown().map(KeywordStatistics::getStatementCount).orElse(0);

        Set<SourceNode> nodes = getFixtureLoggingNodes(testCase);

        double metric = (double)nodes.size() / (double)statements;

        return new SmellResult(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, metric, nodes);
    }

    private Set<SourceNode> getFixtureLoggingNodes(TestCase testCase){
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.LOG);

        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));
        testCase.getTearDown().ifPresent(s -> visitor.visit(s, new PathMemory()));

        return visitor.getNodes();
    }
}
