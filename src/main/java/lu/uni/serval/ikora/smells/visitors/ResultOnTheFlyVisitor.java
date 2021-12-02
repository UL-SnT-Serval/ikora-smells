package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;

public class ResultOnTheFlyVisitor extends SmellVisitor {
    private int expectedCount = 0;
    private int onTheFlyCount = 0;

    public int getOnTheFly() {
        return onTheFlyCount;
    }

    public int getExpected() {
        return expectedCount;
    }

    @Override
    public void visit(Argument argument, VisitorMemory memory) {
        if(argument.getType().getName().equalsIgnoreCase("expected")){
            processOnTheFly(argument);
            ++expectedCount;
        }
    }

    private void processOnTheFly(Argument argument){
        if(!argument.isVariable()){
            return;
        }

        final Variable variable = (Variable) argument.getDefinition();

        for(Dependable definition: variable.getDefinition(Link.Import.BOTH)){
           if((definition instanceof UserKeyword) || (definition instanceof Assignment)){
               ++onTheFlyCount;
               addNode((SourceNode) definition);
               addNode(variable);
           }
        }
    }
}
