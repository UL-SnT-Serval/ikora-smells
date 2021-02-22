package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.model.ForLoop;
import lu.uni.serval.ikora.model.Keyword;
import lu.uni.serval.ikora.model.KeywordCall;

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
