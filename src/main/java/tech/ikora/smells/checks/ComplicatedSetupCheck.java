package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ComplicatedSetupCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        double metric = Double.NaN;

        Set<SourceNode> nodes = new HashSet<>();

        if(testCase.getSetup().isPresent()){
            int setupSize = KeywordStatistics.getSequenceSize(testCase.getSetup().get());
            int testCaseSize = KeywordStatistics.getSequenceSize(testCase);

            nodes.add(testCase.getSetup().get());

            metric = (double)setupSize / (double)(setupSize + testCaseSize);
        }

        return new SmellResult(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, metric, nodes);
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        for(Action action: change.getActions()){
            final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(action.getLeft());
            final Optional<SourceNode> newNode = NodeUtils.toSourceNode(action.getRight());

            if(oldNode.isPresent()
                    && newNode.isPresent()
                    && nodes.contains(oldNode.get())){
                int oldSize = KeywordStatistics.getSequenceSize(oldNode.get());
                int newSize = KeywordStatistics.getSequenceSize(newNode.get());

                return oldSize > newSize;
            }
        }

        return false;
    }
}
