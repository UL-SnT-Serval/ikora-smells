package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.analytics.clones.Clones;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.model.KeywordDefinition;
import lu.uni.serval.ikora.model.UserKeyword;

public class CloneVisitor extends SmellVisitor {
    private int totalKeywordsCounter;

    private final Clones<KeywordDefinition> clones;

    public CloneVisitor(Clones<KeywordDefinition> clones){
        this.clones = clones;
    }

    public int getCloneCount() {
        return getNodes().size();
    }

    public int getTotalKeywordsCount() {
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
