package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.smells.utils.NLPUtils;

import java.util.Set;
import java.util.stream.Collectors;

public class NarcissisticCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = collectStepsUsingPersonalPronoun(testCase);

        double rawValue = nodes.size();
        double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.NARCISSISTIC, rawValue, normalizedValue, nodes);
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .flatMap(t -> collectStepsUsingPersonalPronoun(t).stream())
                .collect(Collectors.toSet());
    }


    private Set<SourceNode> collectStepsUsingPersonalPronoun(TestCase testCase){
        return testCase.getSteps().stream().filter(NLPUtils::isUsingPersonalPronoun)
                .map(SourceNode.class::cast)
                .collect(Collectors.toSet());
    }
}
