package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class ActionCounterVisitor extends TreeVisitor {
    private int actionCount = 0;
    private int totalCallCount = 0;
    private Keyword.Type type;

    public ActionCounterVisitor(Keyword.Type type){
        this.type = type;
    }

    public int getActionCount() {
        return actionCount;
    }

    public int getTotalCallCount() {
        return totalCallCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(isAction(call)){
            ++actionCount;
        }

        ++totalCallCount;

        super.visit(call, memory);
    }

    private boolean isAction(KeywordCall call){
        return call.getKeyword().filter(keyword -> keyword.getType() == this.type).isPresent();
    }
}
