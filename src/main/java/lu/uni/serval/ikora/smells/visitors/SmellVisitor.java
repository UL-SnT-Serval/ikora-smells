package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.analytics.visitor.TreeVisitor;
import lu.uni.serval.ikora.model.*;

import java.util.HashSet;
import java.util.Optional;
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
