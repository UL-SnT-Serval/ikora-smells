package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;

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
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = new HashSet<>();

        for(TestCase testCase: file.getTestCases()){
            final SmellResult smellResult = computeMetric(testCase, configuration);
            if(smellResult.getNormalizedValue() < configuration.getSetupTestRatio()){
                nodes.addAll(smellResult.getNodes());
            }
        }

        return nodes;
    }

}
