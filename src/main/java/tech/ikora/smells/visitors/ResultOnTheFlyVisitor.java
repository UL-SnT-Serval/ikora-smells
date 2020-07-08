package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.model.*;
import tech.ikora.types.BaseType;
import tech.ikora.types.BaseTypeList;

import java.util.Optional;

public class ResultOnTheFlyVisitor extends SmellVisitor {
    private int expected = 0;

    public int getOnTheFly() {
        return getNodes().size();
    }

    public int getExpected() {
        return expected;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<SourceNode> expected = getExpected(call);

        if(expected.isPresent()){
            final Class<?> expectedClass = expected.get().getClass();

            if(Variable.class.isAssignableFrom(expectedClass)){
                processVariable((Variable)expected.get());
            }

            ++this.expected;
        }

        super.visit(call, memory);
    }

    private Optional<SourceNode> getExpected(KeywordCall call) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(!keyword.isPresent()){
            return Optional.empty();
        }

        final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();
        final Optional<BaseType> expected = argumentTypes.stream()
                .filter(t -> t.getName().equalsIgnoreCase("expected"))
                .findFirst();

        if(!expected.isPresent()){
            return Optional.empty();
        }

        final int index = argumentTypes.indexOf(expected.get());
        if(!call.getArgumentList().isExpendedUntilPosition(index)){
            return Optional.empty();
        }

        return call.getArgumentList().get(index).getDefinition();
    }

    private void processVariable(Variable variable){
        for(Node definition: variable.getDefinition(Link.Import.BOTH)){
           if(isComputed(definition)){
               addNode(variable);
           }
        }
    }

    private boolean isComputed(Node definition){
        if(!(definition instanceof Variable)){
            return false;
        }

        final SourceNode parent = getParent((Variable) definition);
        return (parent instanceof UserKeyword) || (parent instanceof Assignment);
    }

    private SourceNode getParent(Variable variable){
        final SourceNode astParent = variable.getAstParent();

        if(!(astParent instanceof NodeList)){
            return null;
        }

        return astParent.getAstParent();
    }
}
