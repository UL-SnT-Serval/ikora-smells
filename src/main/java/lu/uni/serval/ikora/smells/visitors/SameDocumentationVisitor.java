package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.KeywordDefinition;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.core.model.UserKeyword;

public class SameDocumentationVisitor extends SmellVisitor {
    private int totalKeywordsCounter = 0;

    public int getSameDocumentationKeywords(){
        return getNodes().size();
    }

    public int getTotalKeywords() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(TestCase testCase, VisitorMemory memory) {
        check(testCase);
        super.visit(testCase, memory);
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        check(keyword);
        super.visit(keyword, memory);
    }

    public void check(KeywordDefinition keyword){
        if(keyword.getDocumentation().toString().equalsIgnoreCase(keyword.getName())){
            addNode(keyword.getDocumentation());
        }

        ++totalKeywordsCounter;
    }
}
