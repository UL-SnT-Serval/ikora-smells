package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseType;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.utils.ArgumentUtils;

import java.util.Optional;

public class ResultOnTheFlyVisitor extends SmellVisitor {
    private int expected = 0;
    private int onTheFly = 0;

    public int getOnTheFly() {
        return onTheFly;
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
        if(!ArgumentUtils.isExpendedUntilPosition(call.getArgumentList(), index)){
            return Optional.empty();
        }

        return Optional.of(call.getArgumentList().get(index).getDefinition());
    }

    private void processVariable(Variable variable){
        for(Node definition: variable.getDefinition(Link.Import.BOTH)){
           if(isComputed(definition)){
               ++onTheFly;
               addNode((SourceNode) definition);
               addNode(variable);
           }
        }
    }

    private boolean isComputed(Node definition){
        return (definition instanceof UserKeyword) || (definition instanceof Assignment);
    }
}
