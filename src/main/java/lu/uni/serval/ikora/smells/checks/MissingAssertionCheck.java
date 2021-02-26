package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.CollectCallsByTypeVisitor;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final CollectCallsByTypeVisitor visitor = new CollectCallsByTypeVisitor(Keyword.Type.ASSERTION);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getNodes().size();
        double normalized = visitor.getNodes().isEmpty() ? 1. : 0.;

        return new SmellResult(SmellMetric.Type.MISSING_ASSERTION, rawValue, normalized, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        if(edit.getType() == Edit.Type.ADD_STEP && isAddAssertion((Step)edit.getRight())){
            return true;
        }

        return edit.getType() == Edit.Type.ADD_USER_KEYWORD && isAddAssertion((UserKeyword) edit.getRight());
    }

    private boolean isAddAssertion(Step step){
        return step.getKeywordCall()
                .flatMap(Step::getKeywordCall)
                .map(KeywordCall::getKeywordType)
                .map(t -> t == Keyword.Type.ASSERTION)
                .orElse(false);
    }

    private boolean isAddAssertion(UserKeyword keyword){
        return keyword.getSteps().stream()
                .anyMatch(this::isAddAssertion);
    }
}
