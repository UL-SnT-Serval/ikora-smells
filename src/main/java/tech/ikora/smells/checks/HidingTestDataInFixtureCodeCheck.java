package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Set;

public class HidingTestDataInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.GET);
        testCase.getSetup().ifPresent(s -> visitor.visit(s, new PathMemory()));

        double metric = visitor.getTotalVisited() > 0 ? (double)visitor.getNodes().size() / (double)visitor.getTotalVisited() : 0.;

        return new SmellResult(SmellMetric.Type.HIDING_TEST_DATA_IN_FIXTURE_CODE, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
    }
}
