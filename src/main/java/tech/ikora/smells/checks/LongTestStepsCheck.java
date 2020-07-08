package tech.ikora.smells.checks;

import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.SourceNode;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;

import java.util.HashSet;
import java.util.Set;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            if(KeywordStatistics.getSequenceSize(step) > 50){
                nodes.add(step);
            }
        }

        double metric = (double)nodes.size() / (double)testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LONG_TEST_STEPS, metric, nodes);
    }
}
