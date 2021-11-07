package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.Node;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ConditionalTestLogicVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.List;

public class ConditionalTestLogicCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final ConditionalTestLogicVisitor visitor = new ConditionalTestLogicVisitor();
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getNodes().size() ;
        double normalizedValue = rawValue / visitor.getConditionsCount();

        return new SmellResult(SmellMetric.Type.CONDITIONAL_TEST_LOGIC, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public List<Node> collectInstances(SourceFile file) {
        return null;
    }
}
