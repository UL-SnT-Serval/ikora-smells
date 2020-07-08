package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

public class HidingTestDataInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.GET);
        visitor.visit(testCase.getSetup(), new PathMemory());

        double metric = (double)visitor.getNodes().size() / (double)visitor.getTotalVisited();

        return new SmellResult(SmellMetric.Type.HIDING_TEST_DATA_IN_FIXTURE_CODE, metric, visitor.getNodes());
    }
}
