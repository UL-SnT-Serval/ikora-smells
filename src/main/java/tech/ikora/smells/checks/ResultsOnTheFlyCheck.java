package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.ResultOnTheFlyVisitor;

import java.util.Set;

public class ResultsOnTheFlyCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        ResultOnTheFlyVisitor visitor = new ResultOnTheFlyVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getOnTheFly() / (double)visitor.getExpected();

        return new SmellResult(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return false;
    }
}
