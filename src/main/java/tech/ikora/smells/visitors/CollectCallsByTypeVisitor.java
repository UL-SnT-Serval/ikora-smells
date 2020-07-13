package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;
import tech.ikora.smells.NodeUtils;

public class CollectCallsByTypeVisitor extends SmellVisitor {
    private final Keyword.Type type;
    private int totalVisited;


    public CollectCallsByTypeVisitor(Keyword.Type type){
        this.type = type;
        this.totalVisited = 0;
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(NodeUtils.isType(call, this.type, false)){
            addNode(call);
        }
        ++totalVisited;

        super.visit(call, memory);
    }
}
