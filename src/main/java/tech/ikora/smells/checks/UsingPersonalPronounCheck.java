package tech.ikora.smells.checks;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class UsingPersonalPronounCheck implements SmellCheck {
    @Override
    public SmellMetric computeMetric(TestCase testCase, SmellDetector detector) {
        long personalPronoun = calculateUsingPersonalPronoun(testCase.getSteps());
        double metric = (double)personalPronoun / (double)testCase.getSteps().size();

        return new SmellMetric(SmellMetric.Type.USING_PERSONAL_PRONOUN, metric);
    }

    private long calculateUsingPersonalPronoun(List<Step> steps){
        return steps.stream().filter(this::isUsingPersonalPronoun).count();
    }

    private boolean isUsingPersonalPronoun(Step step){
        final String text = step.getName();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos");
        props.setProperty("coref.algorithm", "neural");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        for(CoreSentence sentence: document.sentences()){
            if(findPronoun(sentence).isPresent()){
                return true;
            }
        }

        return false;
    }

    private Optional<String> findPronoun(CoreSentence sentence){
        for(int i = 0; i < sentence.tokens().size(); ++i){
            if(sentence.posTags().get(i).startsWith("VB")){
                break;
            }

            if(sentence.posTags().get(i).equals("PRP")){
                return Optional.of(sentence.tokens().get(i).value());
            }
        }

        return Optional.empty();
    }
}
