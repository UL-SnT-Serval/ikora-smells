package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
    Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration);
}
