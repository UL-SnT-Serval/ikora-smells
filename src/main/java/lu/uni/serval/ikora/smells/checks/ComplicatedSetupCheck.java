package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.model.KeywordCall;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.KeywordStatistics;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ComplicatedSetupCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        double rawValue = Double.NaN;
        double normalizedValue = Double.NaN;

        final Set<SourceNode> nodes = new HashSet<>();
        final Optional<KeywordCall> setup = testCase.getSetup();

        if(setup.isPresent()){
            int setupSize = KeywordStatistics.getSequenceSize(setup.get());
            int testCaseSize = KeywordStatistics.getSequenceSize(testCase);

            nodes.add(setup.get());

            rawValue = setupSize;
            normalizedValue = rawValue / (setupSize + testCaseSize);
        }

        return new SmellResult(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, rawValue, normalizedValue, nodes);
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
