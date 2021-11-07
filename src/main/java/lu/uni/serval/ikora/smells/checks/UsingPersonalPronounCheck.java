package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.smells.utils.NLPUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UsingPersonalPronounCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = collectStepsUsingPersonalPronoun(testCase.getSteps());

        double rawValue = nodes.size();
        double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.USING_PERSONAL_PRONOUN, rawValue, normalizedValue, nodes);
    }

    @Override
    public List<Node> collectInstances(SourceFile file) {
        return null;
    }

    private Set<SourceNode> collectStepsUsingPersonalPronoun(List<Step> steps){
        return steps.stream().filter(NLPUtils::isUsingPersonalPronoun)
                .map(SourceNode.class::cast)
                .collect(Collectors.toSet());
    }
}
