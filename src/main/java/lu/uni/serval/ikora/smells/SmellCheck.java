package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.*;

import java.util.List;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
    List<Node> collectInstances(SourceFile file);
}
