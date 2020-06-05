package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class GetterCounterVisitor extends TreeVisitor {
    private int getterCount = 0;

    public int getGetterCount() {
        return getterCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(isGetter(call)){
            ++getterCount;
        }

        super.visit(call, memory);
    }

    private boolean isGetter(KeywordCall call){
        return call.getKeyword().filter(keyword -> keyword.getType() == Keyword.Type.GET).isPresent();
    }
}
