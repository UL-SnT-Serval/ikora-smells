package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Collections;
import java.util.Set;

public class LoggingInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        Set<SourceNode> nodes = Collections.emptySet();
        double metric = Double.NaN;

        if(testCase.getSetup().isPresent() || testCase.getTearDown().isPresent()){
            int statements = testCase.getSetup().map(KeywordStatistics::getStatementCount).orElse(1) - 1
                    + testCase.getTearDown().map(KeywordStatistics::getStatementCount).orElse(1) - 1;

            nodes = getFixtureLoggingNodes(testCase);
            metric = (double)nodes.size() / (double)statements;
        }

        return new SmellResult(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, metric, nodes);
    }

    @Override
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(action, nodes, Action.Type.REMOVE_STEP);
    }

    private Set<SourceNode> getFixtureLoggingNodes(TestCase testCase){
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.LOG);

        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));
        testCase.getTearDown().ifPresent(s -> visitor.visit(s, new PathMemory()));

        return visitor.getNodes();
    }
}
