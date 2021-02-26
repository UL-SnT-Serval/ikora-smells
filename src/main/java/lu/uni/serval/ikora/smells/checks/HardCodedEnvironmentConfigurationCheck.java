package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.HardCodedValuesVisitor;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.Keyword;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;

import java.util.Set;

public class HardCodedEnvironmentConfigurationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final HardCodedValuesVisitor visitor = new HardCodedValuesVisitor(Keyword.Type.CONFIGURATION);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getNumberHardcodedValues();
        double normalizedValue = rawValue / (double) visitor.getTotalArguments();

        return new SmellResult(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.CHANGE_VALUE_TYPE);
    }
}
