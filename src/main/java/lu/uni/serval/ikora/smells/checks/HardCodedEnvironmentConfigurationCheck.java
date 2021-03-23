package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.HardCodedValuesVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.TestCase;

public class HardCodedEnvironmentConfigurationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final HardCodedValuesVisitor visitor = new HardCodedValuesVisitor(Keyword.Type.CONFIGURATION);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getNumberHardcodedValues();
        double normalizedValue = rawValue / (double) visitor.getTotalArguments();

        return new SmellResult(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, rawValue, normalizedValue, visitor.getNodes());
    }
}
