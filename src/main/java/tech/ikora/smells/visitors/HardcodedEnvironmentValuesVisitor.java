package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.*;

public class HardcodedEnvironmentValuesVisitor extends SmellVisitor {
    private int totalArguments = 0;

    public int getNumberHardcodedValues() {
        return getNodes().size();
    }

    public int getTotalArguments(){
        return totalArguments;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        if(isConfiguration(call)){
            for(Argument argument: call.getArgumentList()){
                if(argument.isType(Literal.class)){
                    addNode(argument.getDefinition());
                }

                ++totalArguments;
            }

        }

        super.visit(call, memory);
    }

    private boolean isConfiguration(KeywordCall call) {
        return call.getKeywordType() == Keyword.Type.CONFIGURATION;
    }
}
