package tech.ikora.smells;

import tech.ikora.analytics.Action;
import tech.ikora.model.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
    boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration);

    static boolean isFix(Action action, Set<SourceNode> nodes, Action.Type... types){
        if(Arrays.stream(types).noneMatch(t -> action.getType() == t)){
            return false;
        }

        final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(action.getLeft());
        return oldNode.isPresent() && nodes.contains(oldNode.get());
    }
}
