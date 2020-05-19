package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.MissingAssertionVisitor;

public class MissingAssertionCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {

        int missingAssertionSteps = 0;

        for(Step step: testCase.getSteps()){
            MissingAssertionVisitor visitor = new MissingAssertionVisitor();
            visitor.visit(step, new PathMemory());

            if(visitor.getAssertionCount() == 0){
                ++missingAssertionSteps;
            }
        }

        double metric = (double)missingAssertionSteps / (double)testCase.getSteps().size();

        return new SmellMetric(SmellMetric.Type.MISSING_ASSERTION, metric);
    }
}
