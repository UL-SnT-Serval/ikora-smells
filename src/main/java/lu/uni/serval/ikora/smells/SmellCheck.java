package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.*;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
}
