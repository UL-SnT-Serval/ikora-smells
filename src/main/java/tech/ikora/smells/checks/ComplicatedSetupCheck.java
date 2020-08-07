package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.KeywordStatistics;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;

import java.util.HashSet;
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
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
            if(nodes.contains(edit.getLeft())){
                int oldSize = KeywordStatistics.getSequenceSize(edit.getLeft());
                int newSize = KeywordStatistics.getSequenceSize(edit.getRight());

                return oldSize > newSize;
            }

        return false;
    }
}
