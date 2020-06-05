package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.GetterCounterVisitor;

public class HidingTestDataInFixtureCodeCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        GetterCounterVisitor visitor = new GetterCounterVisitor();
        visitor.visit(testCase.getSetup(), new PathMemory());

        return new SmellMetric(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, visitor.getGetterCount());
    }
}
