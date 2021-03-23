package lu.uni.serval.ikora.smells.utils;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lu.uni.serval.ikora.core.model.Step;

import java.util.Optional;
import java.util.Properties;

public class NLPUtils {
    public static boolean isUsingPersonalPronoun(Step step){
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

    public static Optional<String> findPronoun(CoreSentence sentence){
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
