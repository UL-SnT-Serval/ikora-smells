package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class MissingAssertionVisitor extends TreeVisitor {
    private int assertionCount;

    public int getAssertionCount(){
        return assertionCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(k -> {
            if(k.getType() == Keyword.Type.ASSERTION){
                ++this.assertionCount;
            }
        });

        super.visit(call, memory);
    }
}
