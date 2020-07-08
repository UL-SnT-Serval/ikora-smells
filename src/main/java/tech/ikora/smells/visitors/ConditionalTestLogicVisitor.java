package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.ForLoop;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class ConditionalTestLogicVisitor extends SmellVisitor {
    private int conditionsCount = 0;

    public int getConditionsCount(){
        return this.conditionsCount;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        call.getKeyword().ifPresent(k -> {
            if(k.getType() == Keyword.Type.CONTROL_FLOW){
                ++this.conditionsCount;
                addNode(call);
            }
        });

        super.visit(call, memory);
    }

    @Override
    public void visit(ForLoop forLoop, VisitorMemory memory) {
        ++this.conditionsCount;
        addNode(forLoop);

        super.visit(forLoop, memory);
    }
}
