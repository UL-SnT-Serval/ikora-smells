package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.*;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;

import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

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

    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return nodes.contains(edit.getLeft()) && NodeUtils.isCallType(edit.getRight(), Keyword.Type.ASSERTION, true);
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
