package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.analytics.visitor.VisitorUtils;
import tech.ikora.libraries.builtin.keywords.Sleep;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

import java.util.Optional;

public class ResourceOptimismVisitor extends TreeVisitor {
    private int sleepCounter = 0;

    public int getNumberSleepCalls() {
        return sleepCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            if(keyword.get() instanceof Sleep){
                ++sleepCounter;
            }
        }

        super.visit(call, memory);
    }
}
