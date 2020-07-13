package tech.ikora.smells.visitors;

import org.apache.commons.lang3.tuple.Pair;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.builder.ValueResolver;
import tech.ikora.model.*;
import tech.ikora.smells.utils.LocatorUtils;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;



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
            final ArgumentList argumentList = call.getArgumentList();
            final BaseTypeList argumentTypes = keyword.get().getArgumentTypes();

            if(argumentTypes.containsType(LocatorType.class)){
                List<Integer> locatorIndexes = argumentTypes.findAll(LocatorType.class);

                for(int index: locatorIndexes){
                    if(argumentList.isExpendedUntilPosition(index)){
                        final Argument argument = argumentList.get(index);
                        for(Pair<String, SourceNode> value: getArgumentValues(argument)){
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

    private List<Pair<String, SourceNode>> getArgumentValues(Argument argument){
        List<Pair<String, SourceNode>> values = new ArrayList<>();

        for(Node node: ValueResolver.getValueNodes(argument)){
            if(node instanceof Literal){
                values.add(Pair.of(node.getName(), (SourceNode) node));
            }
            else if(node instanceof Argument){
                values.add(Pair.of(node.getName(), (SourceNode) node));
            }
            else if (node instanceof LibraryVariable){
                values.add(Pair.of(node.getName(), argument));
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

    private List<Pair<String, SourceNode>> getAssignmentValue(final VariableAssignment assignment){
        final List<List<String>> values = new ArrayList<>();

        for(Argument argument: assignment.getValues()){
            values.add(getArgumentValues(argument).stream().map(Pair::getLeft).collect(Collectors.toList()));
        }

        return Permutations.permutations(values).stream()
                .map(v -> Pair.of(String.join("\t", v), (SourceNode)assignment))
                .collect(Collectors.toList());
    }

    private List<Pair<String, SourceNode>> getParameterValue(final Variable variable){
        final Optional<UserKeyword> userKeyword = ValueResolver.getUserKeywordFromArgument(variable);

        if(!userKeyword.isPresent()){
            return Collections.emptyList();
        }

        final List<Pair<String, SourceNode>> values = new ArrayList<>();
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
}
