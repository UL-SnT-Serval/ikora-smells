package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;
import tech.ikora.model.Step;
import tech.ikora.model.UserKeyword;

import java.util.List;
import java.util.Optional;

public class OneActionVisitor extends TreeVisitor {
    private int singleActionCount;
    private int keywordsCount;
    private Keyword.Type type;

    public OneActionVisitor(Keyword.Type type){
        this.singleActionCount = 0;
        this.keywordsCount = 0;
        this.type = type;
    }

    public int getSingleActionCount(){
        return singleActionCount;
    }

    public int getKeywordsCount() {
        return keywordsCount;
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(isSingleActionKeyword(keyword.getSteps())){
            ++singleActionCount;
        }

        ++keywordsCount;

        super.visit(keyword, memory);
    }

    private boolean isSingleActionKeyword(List<Step> steps){
        return steps.stream()
                .map(Step::getKeywordCall)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(KeywordCall::getKeyword)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Keyword::getType)
                .filter(t -> t != Keyword.Type.LOG)
                .anyMatch(t -> t != type);
    }
}
