package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;
import tech.ikora.smells.visitors.CollectCallsByTypeVisitor;

import java.util.Optional;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        int totalAssertions = visitor.getNodes().size();
        long conditionalAssertions = visitor.getNodes().stream()
                .map(n -> (KeywordCall)n)
                .filter(this::isConditional)
                .count();

        double metric = totalAssertions > 0 ? (double)conditionalAssertions / (double)totalAssertions : 0.0;

        return new SmellResult(SmellMetric.Type.CONDITIONAL_ASSERTION, metric, visitor.getNodes());
    }

    private boolean isConditional(KeywordCall assertion) {
        SourceNode parent = assertion.getAstParent();

        if(KeywordCall.class.isAssignableFrom(parent.getClass())){
            Optional<Keyword> keywordOptional = ((KeywordCall)parent).getKeyword();

            if(keywordOptional.isPresent()){
                return keywordOptional.get().getType() == Keyword.Type.CONTROL_FLOW;
            }
        }

        return false;
    }
}
