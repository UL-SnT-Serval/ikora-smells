package tech.ikora.smells;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.model.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellDetector detector);
    boolean isFix(Difference change, Set<SourceNode> nodes);
}
