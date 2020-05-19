package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;

public class ComplicatedSetupCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        double metric = 0.0;

        if(testCase.getSetup() != null){
            int setupSize = KeywordStatistics.getSequenceSize(testCase.getSetup());
            int testCaseSize = KeywordStatistics.getSequenceSize(testCase);

            metric = (double)setupSize / (double)(setupSize + testCaseSize);
        }

        return new SmellMetric(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, metric);
    }
}
