package tech.ikora.smells.visitors;

import tech.ikora.analytics.clones.Clones;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.UserKeyword;

public class CloneVisitor extends SmellVisitor {
    private int totalKeywordsCounter;

    private final Clones<KeywordDefinition> clones;

    public CloneVisitor(Clones<KeywordDefinition> clones){
        this.clones = clones;
    }

    public int getCloneCount() {
        return getNodes().size();
    }

    public int getTotalKeywordsCounter() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(this.clones.getCloneType(keyword) != Clones.Type.NONE){
            addNode(keyword);
        }
        ++totalKeywordsCounter;

        super.visit(keyword, memory);
    }
}
