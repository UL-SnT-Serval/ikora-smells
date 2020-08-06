package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.HardCodedValuesVisitor;

import java.util.Set;

public class HardcodedValuesCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getNumberHardcodedValues() / (double) visitor.getTotalArguments();

        return new SmellResult(SmellMetric.Type.HARD_CODED_VALUES, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration){
        return SmellCheck.isFix(action, nodes, Action.Type.CHANGE_VALUE_TYPE);
    }
}
