package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.smells.NodeUtils;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

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
