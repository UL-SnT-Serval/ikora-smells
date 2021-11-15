package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LackOfEncapsulationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {

        final Set<SourceNode> nodes = getNodes(testCase);
        double rawValue = nodes.size();
        double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LACK_OF_ENCAPSULATION, rawValue, normalizedValue, nodes);
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .flatMap(t -> getNodes(t).stream())
                .collect(Collectors.toSet());
    }

    private Set<SourceNode> getNodes(TestCase testCase){
        final Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            step.getKeywordCall().flatMap(KeywordCall::getKeyword).ifPresent(keyword -> {
                if (LibraryKeyword.class.isAssignableFrom(keyword.getClass())) {
                    nodes.add(step);
                }
            });
        }

        return nodes;
    }
}
