package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.EagerTestVisitor;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.SourceNode;
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
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
    }
}
