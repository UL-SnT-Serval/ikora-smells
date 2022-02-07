package lu.uni.serval.ikora.smells.utils;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

public class NLPUtils {
    private NLPUtils() {}

    public static boolean isUsingPersonalPronoun(String sentence) {
        if(sentence == null || sentence.isEmpty()){
            return false;
        }

        try {
            final InputStream tokenModelIn = NLPUtils.class.getResourceAsStream("/en-token.bin");

            if(tokenModelIn == null){
                throw new IOException("Failed to load en-token.bin");
            }

            final TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            final Tokenizer tokenizer = new TokenizerME(tokenModel);
            final String[] tokens = tokenizer.tokenize(sentence);

            final InputStream posModelIn = NLPUtils.class.getResourceAsStream("/en-pos-maxent.bin");

            if(posModelIn == null){
                throw new IOException("Failed to load en-pos-maxent.bin");
            }

            final POSModel posModel = new POSModel(posModelIn);
            final POSTaggerME posTagger = new POSTaggerME(posModel);

            for(String tag: posTagger.tag(tokens)) {
                if(tag.equals("PRP")) return true;
                if(tag.startsWith("N") || tag.startsWith("V")) return false;
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }
}
