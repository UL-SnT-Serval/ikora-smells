package tech.ikora.smells.visitors;

import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.model.*;
import tech.ikora.smells.utils.Permutations;
import tech.ikora.smells.utils.css.parser.SelectorParser;
import tech.ikora.smells.utils.css.selector.CompoundSelector;
import tech.ikora.smells.utils.css.selector.Selector;
import tech.ikora.types.BaseTypeList;
import tech.ikora.types.LocatorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComplexLocatorVisitor extends TreeVisitor {
    private int complexLocators = 0;
    private int locators = 0;

    public int getComplexLocators() {
        return complexLocators;
    }

    public int getLocators() {
        return locators;
    }

    @Override
    public void visit(KeywordCall call, VisitorMemory memory) {
        final Optional<Keyword> keyword = call.getKeyword();

        if(keyword.isPresent()){
            final ArgumentList argumentList = call.getArgumentList();
            final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();

            if(argumentTypes.containsType(LocatorType.class)){
                List<Integer> locatorIndexes = argumentTypes.findAll(LocatorType.class);

                for(int index: locatorIndexes){
                    if(argumentList.isExpendedUntilPosition(index)){
                        final Argument argument = argumentList.get(index);
                        for(String value: getArgumentValues(argument)){
                            if(isComplex(value)){
                                ++complexLocators;
                            }

                            ++locators;
                        }
                    }
                }
            }
        }

        super.visit(call, memory);
    }

    private List<String> getArgumentValues(Argument argument){
        List<String> values = new ArrayList<>();

        for(Node node: ValueResolver.getValueNodes(argument)){
            if(node instanceof Literal){
                values.add(node.getName());
            }
            else if(node instanceof Argument){
                values.add(node.getName());
            }
            else if (node instanceof LibraryVariable){
                values.add(node.getName());
            }
            else if(node instanceof VariableAssignment){
                values.addAll(getAssignmentValue((VariableAssignment)node));
            }
            else if (node instanceof Variable){
                values.addAll(getParameterValue((Variable)node));
            }
        }

        return values;
    }

    private List<String> getAssignmentValue(final VariableAssignment assignment){
        final List<List<String>> values = new ArrayList<>();

        for(Argument argument: assignment.getValues()){
            values.add(getArgumentValues(argument));
        }

        return Permutations.permutations(values).stream()
                .map(v -> String.join("\t", v))
                .collect(Collectors.toList());
    }

    private List<String> getParameterValue(final Variable variable){
        final Optional<UserKeyword> userKeyword = ValueResolver.getUserKeywordFromArgument(variable);

        if(!userKeyword.isPresent()){
            return Collections.emptyList();
        }

        final List<String> values = new ArrayList<>();
        final int position = userKeyword.get().getArguments().indexOf(variable);

        for(Node node: userKeyword.get().getDependencies()){
            if(!(node instanceof KeywordCall)){
                continue;
            }

            final ArgumentList argumentList = ((KeywordCall) node).getArgumentList();

            if(!argumentList.isExpendedUntilPosition(position)){
                continue;
            }

            final Argument argument = argumentList.get(position);
            values.addAll(getArgumentValues(argument));
        }

        return values;
    }

    private boolean isComplex(final String value){
        final Selector selector = SelectorParser.parse(value);
        return getNodeNumber(selector) > 4;
    }

    private int getNodeNumber(final Selector selector){
        CompoundSelector compoundSelector = selector.getCompoundSelector();

        int size = 1;
        while (compoundSelector.previous != null){
            compoundSelector = compoundSelector.previous.getSecond();
            ++size;
        }

        return size;
    }
}
