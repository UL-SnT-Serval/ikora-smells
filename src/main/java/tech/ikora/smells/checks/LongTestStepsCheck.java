package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
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
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        final KeywordDefinition previousStep = getPreviousStep(edit, nodes);

        if(previousStep == null){
            return false;
        }

        return KeywordStatistics.getSequenceSize(previousStep) < configuration.getMaximumStepSize();
    }

    private KeywordDefinition getPreviousStep(Edit edit, Set<SourceNode> nodes){
        if(edit.getType() != Edit.Type.REMOVE_STEP){
            return null;
        }

        if(!Step.class.isAssignableFrom(edit.getLeft().getClass())){
            return null;
        }

        return getRelevantStep((Step) edit.getLeft(), nodes);
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
