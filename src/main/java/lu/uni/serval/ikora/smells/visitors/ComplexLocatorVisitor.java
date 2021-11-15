package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.smells.utils.LocatorUtils;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.BaseTypeList;
import lu.uni.serval.ikora.core.types.LocatorType;
import lu.uni.serval.ikora.core.utils.ArgumentUtils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;

public class ComplexLocatorVisitor extends SmellVisitor {
    private int locators = 0;
    private final int maximumLocatorSize;

    public ComplexLocatorVisitor(int maximumLocatorSize){
        this.maximumLocatorSize = maximumLocatorSize;
    }

    public int getComplexLocators() {
        return getNodes().size();
    }

    public int getLocators() {
        return locators;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            final NodeList<Argument> argumentList = call.getArgumentList();
            final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();

            if(argumentTypes.containsType(LocatorType.class)){
                List<Integer> locatorIndexes = argumentTypes.findAll(LocatorType.class);

                for(int index: locatorIndexes){
                    if(ArgumentUtils.isExpendedUntilPosition(argumentList, index)){
                        final Argument argument = argumentList.get(index);
                        visitArgument(argument);
                    }
                }
            }
        }

        super.visit(call, memory);
    }

    private void visitArgument(Argument argument){
        for(Pair<String, SourceNode> value: ArgumentUtils.getArgumentValues(argument)){
            if(LocatorUtils.isComplex(value.getLeft(), this.maximumLocatorSize)){
                addNode(value.getRight());
            }

            ++locators;
        }
    }
}
