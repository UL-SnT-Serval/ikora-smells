package tech.ikora.smells;

import tech.ikora.model.*;

import java.util.*;

public class NodeUtils {
    public static Optional<SourceNode> toSourceNode(Differentiable differentiable){
        if(differentiable == null){
            return Optional.empty();
        }

        return SourceNode.class.isAssignableFrom(differentiable.getClass()) ? Optional.of((SourceNode)differentiable) : Optional.empty();
    }

    public static boolean isCallType(SourceNode sourceNode, Keyword.Type type, boolean allowIndirectCall){
        if(sourceNode instanceof KeywordCall){
            return isType((KeywordCall)sourceNode, type, allowIndirectCall);
        }

        return false;
    }

    public static boolean isSingleAction(UserKeyword keyword, Set<Keyword.Type> types){
        return keyword.getSteps().stream()
                .map(Step::getKeywordCall)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(KeywordCall::getKeyword)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Keyword::getType)
                .filter(t -> t != Keyword.Type.LOG)
                .anyMatch(types::contains);
    }

    public static boolean isType(KeywordCall call, Keyword.Type type, boolean allowIndirectCall){
        final Optional<Keyword> keyword = call.getKeyword();

        if(!keyword.isPresent()){
            return false;
        }

        if(keyword.get().getType() == type){
            return true;
        }

        if(allowIndirectCall && UserKeyword.class.isAssignableFrom(keyword.get().getClass())){
            return isSingleAction((UserKeyword)keyword.get(), Collections.singleton(type));
        }

        return false;
    }
}
