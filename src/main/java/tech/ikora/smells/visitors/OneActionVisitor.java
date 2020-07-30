package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.UserKeyword;
import tech.ikora.smells.NodeUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OneActionVisitor extends SmellVisitor {
    private int keywordsCount;
    private Set<Keyword.Type> types;

    public OneActionVisitor(Keyword.Type... types){
        this.keywordsCount = 0;
        this.types = new HashSet<>(Arrays.asList(types));
    }

    public int getSingleActionCount(){
        return getNodes().size();
    }

    public int getKeywordsCount() {
        return keywordsCount;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(NodeUtils.isSingleAction(keyword, this.types)){
            addNode(keyword);
        }

        ++keywordsCount;

        super.visit(keyword, memory);
    }
}
