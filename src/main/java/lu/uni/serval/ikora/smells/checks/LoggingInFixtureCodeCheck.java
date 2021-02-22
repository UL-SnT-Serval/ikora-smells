package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.KeywordStatistics;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.Keyword;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;

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
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
    }

    private Set<SourceNode> getFixtureLoggingNodes(TestCase testCase){
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.LOG);

        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));
        testCase.getTearDown().ifPresent(s -> visitor.visit(s, new PathMemory()));

        return visitor.getNodes();
    }
}
