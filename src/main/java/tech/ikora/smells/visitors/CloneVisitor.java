package tech.ikora.smells.visitors;

import tech.ikora.analytics.clones.Clone;
import tech.ikora.analytics.clones.Clones;
import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.UserKeyword;

public class CloneVisitor extends TreeVisitor {
    private int cloneCount;
    private int totalKeywordsCounter;

    private final Clones<UserKeyword> clones;

    public CloneVisitor(Clones<UserKeyword> clones){
        this.clones = clones;
    }

    public int getCloneCount() {
        return cloneCount;
    }

    public int getTotalKeywordsCounter() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(this.clones.getCloneType(keyword) != Clone.Type.NONE){
            ++cloneCount;
        }
        ++totalKeywordsCounter;

        super.visit(keyword, memory);
    }
}
