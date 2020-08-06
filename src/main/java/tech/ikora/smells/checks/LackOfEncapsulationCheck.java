package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.model.*;
import tech.ikora.smells.*;

import java.util.HashSet;
import java.util.Set;

public class LackOfEncapsulationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        int[] libraryKeywordCalls = { 0 };

        Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(keyword -> {
                if (LibraryKeyword.class.isAssignableFrom(keyword.getClass())) {
                    ++libraryKeywordCalls[0];
                    nodes.add(step);
                }
            });
        }

        double metric = (double)libraryKeywordCalls[0] / (double)testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LACK_OF_ENCAPSULATION, metric, nodes);
    }

    @Override
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(action, nodes, Action.Type.REMOVE_STEP, Action.Type.CHANGE_STEP);
    }
}
