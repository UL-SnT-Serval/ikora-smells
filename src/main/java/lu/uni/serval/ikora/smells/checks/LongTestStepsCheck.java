package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.*;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            if(KeywordStatistics.getSequenceSize(step) > configuration.getMaximumStepSize()){
                nodes.add(step);
            }
        }

        double rawValue = nodes.size();
        double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LONG_TEST_STEPS, rawValue, normalizedValue, nodes);
    }
}
