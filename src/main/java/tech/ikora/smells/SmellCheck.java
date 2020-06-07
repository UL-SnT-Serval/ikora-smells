package tech.ikora.smells;

import tech.ikora.model.TestCase;

public interface SmellCheck {
    SmellMetric computeMetric(TestCase testCase, SmellDetector detector);
}
