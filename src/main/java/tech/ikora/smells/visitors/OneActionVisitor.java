package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.UserKeyword;
import tech.ikora.smells.NodeUtils;

public class OneActionVisitor extends SmellVisitor {
    private int keywordsCount;
    private Keyword.Type type;

    public OneActionVisitor(Keyword.Type type){
        this.keywordsCount = 0;
        this.type = type;
    }

    public int getSingleActionCount(){
        return getNodes().size();
    }

    public int getKeywordsCount() {
        return keywordsCount;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(NodeUtils.isSingleAction(keyword, this.type)){
            addNode(keyword);
        }

        ++keywordsCount;

        super.visit(keyword, memory);
    }
}
