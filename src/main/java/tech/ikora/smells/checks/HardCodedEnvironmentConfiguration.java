package tech.ikora.smells.checks;

import org.apache.commons.lang3.NotImplementedException;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.SmellResult;

public class HardCodedEnvironmentConfiguration implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        throw new NotImplementedException("Check is not implemented yet: " + this.getClass().getName());
    }
}
