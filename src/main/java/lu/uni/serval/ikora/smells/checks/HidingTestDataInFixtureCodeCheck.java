package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.TestCase;

public class HidingTestDataInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.GET);
        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));

        double rawValue = visitor.getNodes().size();
        double normalizedValue = visitor.getTotalVisited() > 0 ? rawValue / visitor.getTotalVisited() : 0.;

        return new SmellResult(SmellMetric.Type.HIDING_TEST_DATA_IN_FIXTURE_CODE, rawValue, normalizedValue, visitor.getNodes());
    }
}
