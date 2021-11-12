package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.analytics.visitor.FileMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ResultOnTheFlyVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public class OnTheFlyCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final ResultOnTheFlyVisitor visitor = visit(testCase, new PathMemory());

        double rawValue = visitor.getOnTheFly();
        double normalizedValue = rawValue / visitor.getExpected();

        return new SmellResult(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file)).getNodes();
    }

    private ResultOnTheFlyVisitor visit(SourceNode node, VisitorMemory memory){
        final ResultOnTheFlyVisitor visitor = new ResultOnTheFlyVisitor();
        visitor.visit(node, memory);

        return visitor;
    }
}
