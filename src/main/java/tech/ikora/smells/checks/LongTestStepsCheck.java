package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        int largestStep = 0;

        for(Step step: testCase.getSteps()){
            largestStep = Math.max(largestStep, KeywordStatistics.getSequenceSize(step));
        }

        double metric = largestStep < 100 ? (double)largestStep/100.0 : 1.0;

        return new SmellMetric(SmellMetric.Type.LONG_TEST_STEPS, metric);
    }
}
