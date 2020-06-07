package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.CollectAssertionsVisitor;

import java.util.Optional;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        CollectAssertionsVisitor visitor = new CollectAssertionsVisitor();
        visitor.visit(testCase, new PathMemory());

        int totalAssertions = visitor.getAssertions().size();
        long conditionalAssertions = visitor.getAssertions().stream().filter(this::isConditional).count();

        double metric = totalAssertions > 0 ? (double)conditionalAssertions / (double)totalAssertions : 0.0;

        return new SmellMetric(SmellMetric.Type.CONDITIONAL_ASSERTION, metric);
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
