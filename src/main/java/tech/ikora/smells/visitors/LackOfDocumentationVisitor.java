package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.UserKeyword;

public class LackOfDocumentationVisitor extends TreeVisitor {
    private int documentedKeywordsCounter = 0;
    private int totalKeywordsCounter = 0;

    public double getDocumentedKeywordProportion(){
        return (double)documentedKeywordsCounter / (double)totalKeywordsCounter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(!keyword.getDocumentation().isEmpty()){
            ++documentedKeywordsCounter;
        }

        ++totalKeywordsCounter;

        super.visit(keyword, memory);
    }
}
