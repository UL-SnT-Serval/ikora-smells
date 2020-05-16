package tech.ikora.smells.checks;

import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellMetric;

public class ConditionalAssertionCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        throw new NotImplementedException("Check is not implemented yet: " + this.getClass().getName());
    }
}
