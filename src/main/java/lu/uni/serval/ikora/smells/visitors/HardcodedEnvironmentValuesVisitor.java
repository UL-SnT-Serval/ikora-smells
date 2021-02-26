package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;

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
