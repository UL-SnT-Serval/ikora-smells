package tech.ikora.smells;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.model.*;

import java.util.Optional;
import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellDetector detector);
    boolean isFix(Difference change, Set<SourceNode> nodes);

    static boolean isFix(Difference change, Set<SourceNode> nodes, Action.Type... types){
        for(Action action: NodeUtils.getActionsByType(change, types)) {
            final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(action.getLeft());
            final Optional<SourceNode> newNode = NodeUtils.toSourceNode(action.getRight());

            if(oldNode.isPresent()
                    && newNode.isPresent()
                    && nodes.contains(oldNode.get())){
                return true;
            }
        }

        return false;
    }
}
