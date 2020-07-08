package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;

import java.util.Collections;

public class ComplicatedSetupCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        double metric = 0.0;

        if(testCase.getSetup() != null){
            int setupSize = KeywordStatistics.getSequenceSize(testCase.getSetup());
            int testCaseSize = KeywordStatistics.getSequenceSize(testCase);

            metric = (double)setupSize / (double)(setupSize + testCaseSize);
        }

        return new SmellResult(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, metric, Collections.emptySet());
    }
}
