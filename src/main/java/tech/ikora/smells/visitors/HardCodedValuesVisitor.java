package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.*;

import java.util.Optional;

public class HardCodedValuesVisitor extends TreeVisitor {
    private int hardcodedValuesCounter = 0;

    public int getNumberHardcodedValues() {
        return hardcodedValuesCounter;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        for(Argument argument: call.getArgumentList()){
            final Optional<Node> definition = argument.getDefinition();

            if(definition.isPresent()){
                if(definition.get() instanceof Literal){
                    ++hardcodedValuesCounter;
                }
            }
        }

        super.visit(call, memory);
    }
}
