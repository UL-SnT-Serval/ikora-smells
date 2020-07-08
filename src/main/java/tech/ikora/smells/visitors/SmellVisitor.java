package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.model.SourceNode;

import java.util.HashSet;
import java.util.Set;

public abstract class SmellVisitor extends TreeVisitor {
    private Set<SourceNode> nodes = new HashSet<>();

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
