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

import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.EagerTestVisitor;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;

import edu.stanford.nlp.neural.NeuralUtils;

import org.ejml.simple.SimpleMatrix;

import java.util.*;

public class EagerTestCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final EagerTestVisitor visitor = new EagerTestVisitor(testCase.getSteps().size());

        int position = 0;
        for(Step step: testCase.getSteps()){
            visitor.setPosition(position++);
            visitor.visit(step, new PathMemory());
        }

        final List<SimpleMatrix> frequencyVectors = visitor.getFrequencyVectors();

        if(frequencyVectors.isEmpty() || frequencyVectors.get(0).numCols() == 0){
            return new SmellResult(SmellMetric.Type.EAGER_TEST, Double.NaN, Double.NaN, Collections.emptySet());
        }

        int size = frequencyVectors.size();
        int count = 0;
        double sum = 0.0;

        for(int i = 0; i < size; ++i){
            for(int j = i + 1; j< size; ++j){
                double similarity = Math.abs(NeuralUtils.cosine(frequencyVectors.get(i), frequencyVectors.get(j)));

                if(!Double.isNaN(similarity)){
                    sum += similarity;
                    ++count;
                }
            }
        }

        double rawValue = count == 0 ? Double.NaN : sum;
        double normalizedValue = count == 0 ? Double.NaN : 1 -  (rawValue / count);

        return new SmellResult(SmellMetric.Type.EAGER_TEST, rawValue, normalizedValue, new HashSet<>(testCase.getSteps()));
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = new HashSet<>();

        for(TestCase testCase: file.getTestCases()){
            final SmellResult result = computeMetric(testCase, configuration);

            if(result.getNormalizedValue() > configuration.getEagerTestThreshold()){
                nodes.add(testCase);
            }
        }

        return nodes;
    }

}
