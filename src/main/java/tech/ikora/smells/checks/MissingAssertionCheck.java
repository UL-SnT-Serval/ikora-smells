package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.CountCallByTypeVisitor;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {

        int missingAssertionSteps = 0;

        for(Step step: testCase.getSteps()){
            CountCallByTypeVisitor visitor = new CountCallByTypeVisitor(Keyword.Type.ASSERTION);
            visitor.visit(step, new PathMemory());

            if(visitor.getCount() == 0){
                ++missingAssertionSteps;
            }
        }

        double metric = (double)missingAssertionSteps / (double)testCase.getSteps().size();

        return new SmellMetric(SmellMetric.Type.MISSING_ASSERTION, metric);
    }
}
