package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.libraries.builtin.keywords.Sleep;
import tech.ikora.model.Keyword;
import tech.ikora.model.KeywordCall;

import java.util.Optional;

public class SleepCounterVisitor extends SmellVisitor {
    private int syncCounter = 0;

    public int getSleepCalls() {
        return getNodes().size();
    }

    public int getSyncCalls(){
        return syncCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            if(keyword.get().getType() == Keyword.Type.SYNCHRONISATION){
                if(keyword.get() instanceof Sleep){
                    addNode(call);
                }
                ++syncCounter;
            }
        }

        super.visit(call, memory);
    }
}
