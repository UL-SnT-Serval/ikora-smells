package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.utils.ValueFetcher;
import lu.uni.serval.ikora.smells.utils.LocatorUtils;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.types.LocatorType;

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
    public void visit(Argument argument, VisitorMemory memory) {
        if(argument.isType(LocatorType.class)){
            if(ValueFetcher.getValues(argument).stream().anyMatch(v -> LocatorUtils.isComplex(v, this.maximumLocatorSize))){
                addNode(argument);
            }

            ++locators;
        }
    }
}
