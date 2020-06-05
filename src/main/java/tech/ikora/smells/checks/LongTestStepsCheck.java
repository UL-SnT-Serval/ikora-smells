package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        int longSteps = 0;

        for(Step step: testCase.getSteps()){
            if(KeywordStatistics.getSequenceSize(step) > 50){
                ++longSteps;
            }
        }

        double metric = (double)longSteps / (double)testCase.getSteps().size();

        return new SmellMetric(SmellMetric.Type.LONG_TEST_STEPS, metric);
    }
}
