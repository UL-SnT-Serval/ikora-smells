package lu.uni.serval.ikora.smells.checks;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.KeywordStatistics;

import java.util.*;
import java.util.stream.Collectors;

public class LongTestStepsCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = getNodes(testCase, configuration);
        final double rawValue = nodes.size();
        final double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.LONG_TEST_STEPS, rawValue, normalizedValue, nodes);
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return file.getTestCases().stream()
                .flatMap(t -> getNodes(t, configuration).stream())
                .collect(Collectors.toSet());
    }

    private Set<SourceNode> getNodes(TestCase testCase, SmellConfiguration configuration){
        return testCase.getSteps().stream()
                .filter(s -> KeywordStatistics.getSequenceSize(s) > configuration.getMaximumStepSize())
                .collect(Collectors.toSet());
    }
}
