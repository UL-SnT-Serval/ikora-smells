package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.KeywordDefinition;
import tech.ikora.model.SourceNode;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.utils.Cfg;

import java.util.*;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        Set<SourceNode> nodes = new HashSet<>();

        for(Step step: testCase.getSteps()){
            if(KeywordStatistics.getSequenceSize(step) > configuration.getMaximumStepSize()){
                nodes.add(step);
            }
        }

        double metric = (double)nodes.size() / (double)testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LONG_TEST_STEPS, metric, nodes);
    }

    @Override
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration) {
        final KeywordDefinition previousStep = getPreviousStep(action, nodes);

        if(previousStep == null){
            return false;
        }

        return KeywordStatistics.getSequenceSize(previousStep) < configuration.getMaximumStepSize();
    }

    private KeywordDefinition getPreviousStep(Action action, Set<SourceNode> nodes){
        if(action.getType() != Action.Type.REMOVE_STEP){
            return null;
        }

        if(!Step.class.isAssignableFrom(action.getLeft().getClass())){
            return null;
        }

        return getRelevantStep((Step) action.getLeft(), nodes);
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
