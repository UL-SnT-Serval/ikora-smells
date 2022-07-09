package lu.uni.serval.ikora.smells.utils;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2022 University of Luxembourg
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

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;

public class NLPUtils {
    private static final POSTaggerME posTagger;
    private static final Tokenizer tokenizer;

    static {
        try {
            final InputStream tokenModelIn = NLPUtils.class.getResourceAsStream("/en-token.bin");

            if(tokenModelIn == null){
                throw new IOException("Failed to load en-token.bin");
            }

            final TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            tokenizer = new TokenizerME(tokenModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize TokenizerModel for OpenNLP: " + e.getMessage());
        }

        try {
            final InputStream posModelIn = NLPUtils.class.getResourceAsStream("/en-pos-maxent.bin");

            if(posModelIn == null){
                throw new IOException("Failed to load en-pos-maxent.bin");
            }

            final POSModel posModel = new POSModel(posModelIn);
            posTagger = new POSTaggerME(posModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize TokenizerModel for OpenNLP: " + e.getMessage());
        }
    }

    private NLPUtils() {}

    public static boolean isUsingPersonalPronoun(String sentence) {
        if(sentence == null || sentence.isEmpty()){
            return false;
        }

        final String[] tokens = tokenizer.tokenize(sentence);

        for(String tag: posTagger.tag(tokens)) {
            if(tag.equals("PRP")) return true;
            if(tag.startsWith("N") || tag.startsWith("V")) return false;
        }

        return false;
    }
}
