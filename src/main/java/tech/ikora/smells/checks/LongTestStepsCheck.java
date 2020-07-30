package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.SourceNode;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.utils.Cfg;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            if(KeywordStatistics.getSequenceSize(step) > 10){
                nodes.add(step);
            }
        }

        double metric = (double)nodes.size() / (double)testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LONG_TEST_STEPS, metric, nodes);
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes) {
        for(KeywordDefinition keyword: getPreviousSteps(change, nodes)){
            if(KeywordStatistics.getSequenceSize(keyword) < 10){
                return true;
            }
        }

        return false;
    }

    private Set<KeywordDefinition> getPreviousSteps(Difference change, Set<SourceNode> nodes){
        return change.getActions().stream()
                .filter(a -> a.getType() == Action.Type.REMOVE_STEP)
                .filter(a -> Step.class.isAssignableFrom(a.getLeft().getClass()))
                .map(a -> (Step)a.getLeft())
                .map(s -> getRelevantStep(s, nodes))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private KeywordDefinition getRelevantStep(Step step, Set<SourceNode> nodes){
        for(SourceNode node: nodes){
            final Optional<KeywordDefinition> parent = Cfg.getCallerByName(step, node.getNameToken());

            if(parent.isPresent() ){
                return parent.get();
            }
        }

        return null;
    }
}
