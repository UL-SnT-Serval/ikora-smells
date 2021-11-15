package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.UserKeyword;

public class MissingDocumentationVisitor extends SmellVisitor {
    private int totalKeywordsCounter = 0;

    public int getUndocumentedKeywords(){
        return getNodes().size();
    }

    public int getTotalKeywords() {
        return totalKeywordsCounter;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(!keyword.getDocumentation().isPresent() || keyword.getDocumentation().isEmpty()){
            addNode(keyword);
        }

        ++totalKeywordsCounter;

        super.visit(keyword, memory);
    }
}
