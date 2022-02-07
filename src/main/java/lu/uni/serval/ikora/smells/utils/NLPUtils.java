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
