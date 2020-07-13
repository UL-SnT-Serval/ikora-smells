package tech.ikora.smells;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.model.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellDetector detector);
    boolean isFix(Difference change, Set<SourceNode> previousNodes);

    static Set<Action> getActionsByType(Difference change, Action.Type type){
        return change.getActions().stream()
                .filter(a -> a.getType() == type)
                .collect(Collectors.toSet());
    }

    static boolean isCallType(Differentiable differentiable, Keyword.Type type){
        if(differentiable instanceof KeywordCall){
            return ((KeywordCall)differentiable).getKeywordType() == type;
        }

        return false;
    }

    static Optional<SourceNode> toSourceNode(Differentiable differentiable){
        return SourceNode.class.isAssignableFrom(differentiable.getClass()) ? Optional.of((SourceNode)differentiable) : Optional.empty();
    }
}
