package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.libraries.builtin.keywords.Sleep;
import lu.uni.serval.ikora.core.model.Keyword;
import lu.uni.serval.ikora.core.model.KeywordCall;

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
            if(keyword.get().getType() == Keyword.Type.SYNCHRONIZATION){
                if(keyword.get() instanceof Sleep){
                    addNode(call);
                }
                ++syncCounter;
            }
        }

        super.visit(call, memory);
    }
}
