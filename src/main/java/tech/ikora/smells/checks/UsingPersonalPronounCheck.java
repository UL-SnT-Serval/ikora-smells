package tech.ikora.smells.checks;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.model.SourceNode;
import tech.ikora.model.Step;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class UsingPersonalPronounCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        Set<SourceNode> nodes = collectStepsUsingPersonalPronoun(testCase.getSteps());
        double metric = (double)nodes.size() / (double)testCase.getSteps().size();

        return new SmellResult(SmellMetric.Type.USING_PERSONAL_PRONOUN, metric, nodes);
    }

    @Override
    public boolean isFix(Difference change, Set<SourceNode> nodes, SmellConfiguration configuration) {
        for(Action action: change.getActions()){
            final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(action.getLeft());
            final Optional<SourceNode> newNode = NodeUtils.toSourceNode(action.getRight());

            if(oldNode.isPresent()
                    && newNode.isPresent()
                    && nodes.contains(oldNode.get())
                    && !isUsingPersonalPronoun((Step)newNode.get())
            ){
                return true;
            }
        }

        return false;
    }

    private Set<SourceNode> collectStepsUsingPersonalPronoun(List<Step> steps){
        return steps.stream().filter(this::isUsingPersonalPronoun)
                .map(s -> (SourceNode)s)
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
