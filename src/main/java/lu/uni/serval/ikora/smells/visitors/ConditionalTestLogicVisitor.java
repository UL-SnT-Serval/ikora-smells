package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

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
