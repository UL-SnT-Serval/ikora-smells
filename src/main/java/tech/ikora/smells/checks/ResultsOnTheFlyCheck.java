package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.ResultOnTheFlyVisitor;

public class ResultsOnTheFlyCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        ResultOnTheFlyVisitor visitor = new ResultOnTheFlyVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getOnTheFly() / (double)visitor.getExpected();

        return new SmellResult(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, metric, visitor.getNodes());
    }
}
