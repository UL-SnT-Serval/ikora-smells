package tech.ikora.smells.checks;

import tech.ikora.model.KeywordCall;
import tech.ikora.model.LibraryKeyword;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;

public class LackOfEncapsulationCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        int[] libraryKeywordCalls = { 0 };

        for(Step step: testCase.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(keyword -> {
                if (LibraryKeyword.class.isAssignableFrom(keyword.getClass())) {
                    ++libraryKeywordCalls[0];
                }
            });
        }

        double metric = (double)libraryKeywordCalls[0] / (double)testCase.getSteps().size();

        return new SmellMetric(SmellMetric.Type.LACK_OF_ENCAPSULATION, metric);
    }
}
