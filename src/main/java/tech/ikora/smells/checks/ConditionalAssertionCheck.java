package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Set;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.CONTROL_FLOW);
        visitor.visit(testCase, new PathMemory());

        int totalAssertions = visitor.getNodes().size();
        long conditionalAssertions = visitor.getNodes().stream()
                .map(n -> (KeywordCall)n)
                .filter(this::isCallingAssertion)
                .count();

        double metric = totalAssertions > 0 ? (double)conditionalAssertions / (double)totalAssertions : 0.0;

        return new SmellResult(SmellMetric.Type.CONDITIONAL_ASSERTION, metric, visitor.getNodes());
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
