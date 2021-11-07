package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.*;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;

import java.util.List;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.CONTROL_FLOW);
        visitor.visit(testCase, new PathMemory());

        int totalAssertions = visitor.getNodes().size();

        double rawValue = visitor.getNodes().stream()
                .map(KeywordCall.class::cast)
                .filter(this::isCallingAssertion)
                .count();

        double normalizedValue = totalAssertions > 0 ? rawValue / totalAssertions : 0.0;

        return new SmellResult(SmellMetric.Type.CONDITIONAL_ASSERTION, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public List<Node> collectInstances(SourceFile file) {
        return null;
    }

    private boolean isCallingAssertion(KeywordCall assertion) {
        for(Argument argument: assertion.getArgumentList()){
            if(argument.isType(KeywordCall.class)){
                final KeywordCall call = (KeywordCall)argument.getDefinition();
                return NodeUtils.isCallType(call, Keyword.Type.ASSERTION, true);
            }
        }

        return false;
    }
}
