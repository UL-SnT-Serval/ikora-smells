package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.ForLoop;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class ConditionalTestLogicVisitor extends SmellVisitor {
    private int callCount = 0;

    public int getConditionsCount(){
        return this.callCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(k -> {
            if(k.getType() == Keyword.Type.CONTROL_FLOW){
                addNode(call);
            }

            ++this.callCount;
        });

        super.visit(call, memory);
    }
}
