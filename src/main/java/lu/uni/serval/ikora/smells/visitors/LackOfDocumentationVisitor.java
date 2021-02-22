package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.model.UserKeyword;

public class LackOfDocumentationVisitor extends SmellVisitor {
    private int totalKeywordsCounter = 0;

    public int getDocumentedKeyword(){
        return getNodes().size();
    }

    public int getTotalKeywords() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(!keyword.getDocumentation().isEmpty()){
            addNode(keyword);
        }

        ++totalKeywordsCounter;

        super.visit(keyword, memory);
    }
}
