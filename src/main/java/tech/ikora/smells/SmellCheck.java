package tech.ikora.smells;

import tech.ikora.analytics.Difference;
import tech.ikora.model.*;

import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellDetector detector);
    boolean isFix(Difference change, Set<SourceNode> nodes);
}
