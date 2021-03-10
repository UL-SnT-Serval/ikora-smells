package lu.uni.serval.ikora.smells.checks;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.Step;
import lu.uni.serval.ikora.core.model.TestCase;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class UsingPersonalPronounCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final Set<SourceNode> nodes = collectStepsUsingPersonalPronoun(testCase.getSteps());

        double rawValue = nodes.size();
        double normalizedValue = rawValue / testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.USING_PERSONAL_PRONOUN, rawValue, normalizedValue, nodes);
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        if(edit.getType() != Edit.Type.CHANGE_NAME){
            return false;
        }

        return nodes.contains(edit.getLeft()) && !isUsingPersonalPronoun((Step) edit.getRight());
    }

    private Set<SourceNode> collectStepsUsingPersonalPronoun(List<Step> steps){
        return steps.stream().filter(this::isUsingPersonalPronoun)
                .map(SourceNode.class::cast)
                .collect(Collectors.toSet());
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
