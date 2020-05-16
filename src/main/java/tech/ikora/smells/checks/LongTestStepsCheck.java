package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.HardCodedValuesVisitor;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor();
        visitor.visit(testCase, new PathMemory());

        return new SmellMetric(SmellMetric.Type.HARD_CODED_VALUES, visitor.getNumberHardcodedValues());
    }
}
