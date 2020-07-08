package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

public class CollectCallsByTypeVisitor extends SmellVisitor {
    private final Keyword.Type type;
    int totalVisited;

    public CollectCallsByTypeVisitor(Keyword.Type type){
        this.type = type;
        totalVisited = 0;
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(isType(call)){
            addNode(call);
        }

        ++totalVisited;

        super.visit(call, memory);

        super.visit(call, memory);
    }

    private boolean isType(KeywordCall call){
        return call.getKeyword().filter(keyword -> keyword.getType() == this.type).isPresent();
    }
}
