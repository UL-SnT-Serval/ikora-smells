package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.smells.utils.LocatorUtils;
import org.apache.commons.lang3.tuple.Pair;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.model.*;
import lu.uni.serval.ikora.types.BaseTypeList;
import lu.uni.serval.ikora.types.LocatorType;
import lu.uni.serval.ikora.utils.ArgumentUtils;

import java.util.List;
import java.util.Optional;

public class ComplexLocatorVisitor extends SmellVisitor {
    private int locators = 0;

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
                        for(Pair<String, SourceNode> value: ArgumentUtils.getArgumentValues(argument)){
                            if(LocatorUtils.isComplex(value.getLeft(), 4)){
                                addNode(value.getRight());
                            }

                            ++locators;
                        }
                    }
                }
            }
        }

        super.visit(call, memory);
    }




}
