package lu.uni.serval.ikora.smells.utils;

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

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lu.uni.serval.ikora.core.model.Step;

import java.util.Optional;
import java.util.Properties;

public class NLPUtils {
    private NLPUtils() {}

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
