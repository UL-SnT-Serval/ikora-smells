package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.utils.StatementCounter;
import tech.ikora.smells.visitors.HardCodedValuesVisitor;

public class HardcodedValuesCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getNumberHardcodedValues() / (double) StatementCounter.getCount(testCase);

        return new SmellMetric(SmellMetric.Type.HARD_CODED_VALUES, metric);
    }
}
