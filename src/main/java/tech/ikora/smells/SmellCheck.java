package tech.ikora.smells;

import tech.ikora.model.TestCase;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellDetector detector);
}
