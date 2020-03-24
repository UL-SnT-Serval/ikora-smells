package tech.ikora.smells;

import edu.stanford.nlp.neural.NeuralUtils;
import org.ejml.simple.SimpleMatrix;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.visitors.EagerTestVisitor;

import java.util.List;

class EagerTestCheck implements SmellCheck{
    @Override
    public SmellMetric computeMetric(TestCase testCase) {
        EagerTestVisitor visitor = new EagerTestVisitor(testCase.getSteps().size());

        int position = 0;
        for(Step step: testCase.getSteps()){
            visitor.setPosition(position++);
            visitor.visit(step, new PathMemory());
        }

        List<SimpleMatrix> frequencyVectors = visitor.getFrequencyVectors();
        int size = frequencyVectors.size();
        double sum = 0.0;

        for(int i = 0; i < size; ++i){
            for(int j = i + 1; j< size; ++j){
                double similarity = NeuralUtils.cosine(frequencyVectors.get(i), frequencyVectors.get(j));

                if(!Double.isNaN(similarity)){
                    sum += similarity;
                }
            }
        }

        double mean = sum / (double)size;

        return new SmellMetric(SmellMetric.Type.EAGER_TEST, mean);
    }
}
