package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.*;

public class HardCodedValuesVisitor extends SmellVisitor {
    private int totalArguments = 0;

    public int getNumberHardcodedValues() {
        return getNodes().size();
    }

    public int getTotalArguments(){
        return totalArguments;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        for(Argument argument: call.getArgumentList()){
            if(argument.isType(Literal.class)){
                addNode(argument);
            }
            else ++totalArguments;
        }

        super.visit(call, memory);
    }
}
