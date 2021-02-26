package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.TreeVisitor;
import lu.uni.serval.ikora.core.model.*;

import java.util.HashSet;
import java.util.Set;

public abstract class SmellVisitor extends TreeVisitor {
    private final Set<SourceNode> nodes = new HashSet<>();

    protected void addNode(SourceNode node){
        if(node == null){
            return;
        }

        this.nodes.add(node);
    }

    public Set<SourceNode> getNodes() {
        return nodes;
    }
}
