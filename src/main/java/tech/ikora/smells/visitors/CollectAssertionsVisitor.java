package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

import java.util.HashSet;
import java.util.Set;

public class CollectAssertionsVisitor extends TreeVisitor {
    Set<KeywordCall> assertionSet = new HashSet<>();

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(k -> {
            if(k.getType() == Keyword.Type.ASSERTION){
                assertionSet.add(call);
            }
        });

        super.visit(call, memory);
    }

    public Set<KeywordCall> getAssertions(){
        return assertionSet;
    }
}
