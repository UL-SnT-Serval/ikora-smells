package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Argument;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.model.Variable;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.HardCodedValuesVisitor;

import java.util.Optional;
import java.util.Set;

public class HardcodedValuesCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        HardCodedValuesVisitor visitor = new HardCodedValuesVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getNumberHardcodedValues() / (double) visitor.getTotalArguments();

        return new SmellResult(SmellMetric.Type.HARD_CODED_VALUES, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes){
        for(Action action: NodeUtils.getActionsByType(change, Action.Type.CHANGE_STEP_ARGUMENT)) {
            final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(action.getLeft());
            final Optional<SourceNode> newNode = NodeUtils.toSourceNode(action.getRight());

            if(oldNode.isPresent()
                    && newNode.isPresent()
                    && nodes.contains(oldNode.get())
                    && Argument.class.isAssignableFrom(newNode.get().getClass())
                    && Variable.class.isAssignableFrom(((Argument)newNode.get()).getDefinition().getClass())){
                return true;
            }
        }

        return false;
    }
}
