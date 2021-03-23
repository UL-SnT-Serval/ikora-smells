package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.model.*;

import java.util.Arrays;
import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
}
