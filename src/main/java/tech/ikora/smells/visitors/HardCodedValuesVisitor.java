package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HardCodedValuesVisitor extends SmellVisitor {
    private int totalArguments = 0;
    private final Set<Keyword.Type> types;

    public HardCodedValuesVisitor(Keyword.Type... types) {
        this.types = new HashSet<>(Arrays.asList(types));
    }

    public int getNumberHardcodedValues() {
        return getNodes().size();
    }

    public int getTotalArguments(){
        return totalArguments;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(ofInterest(call)){
            for(Argument argument: call.getArgumentList()){
                if(argument.isType(Literal.class)){
                    addNode(argument.getDefinition());
                }

                ++totalArguments;
            }
        }

        super.visit(call, memory);
    }

    private boolean ofInterest(KeywordCall call){
        if(types.isEmpty()){
            return true;
        }

        return call.getKeyword().map(k -> types.contains(k.getType())).orElse(false);
    }
}
