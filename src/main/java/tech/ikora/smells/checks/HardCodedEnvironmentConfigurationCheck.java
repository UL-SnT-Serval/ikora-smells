package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.HardCodedValuesVisitor;

import java.util.Set;

public class HardCodedEnvironmentConfigurationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor(Keyword.Type.CONFIGURATION);
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getNumberHardcodedValues() / (double) visitor.getTotalArguments();

        return new SmellResult(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.CHANGE_VALUE_TYPE);
    }
}
