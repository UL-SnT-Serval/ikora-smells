package lu.uni.serval.ikora.smells.visitors;

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

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.UserKeyword;

import edu.stanford.nlp.simple.Sentence;
import lu.uni.serval.ikora.smells.utils.WordFrequency;
import org.ejml.simple.SimpleMatrix;

import java.util.List;

public class EagerTestVisitor extends SmellVisitor {
    private int position;
    private final WordFrequency wordFrequency;

    public EagerTestVisitor(int size){
        wordFrequency = new WordFrequency(size);
        position = 0;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<SimpleMatrix> getFrequencyVectors(){
        return this.wordFrequency.getFrequencyVectors();
    }

    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        final String name = keyword.getName().toLowerCase();
        final String cleanName = clean(name);
        final Sentence sentence = new Sentence(cleanName);

        wordFrequency.addWords(position, sentence.lemmas());

       super.visit(keyword, memory);
    }

    private String clean(String sentence){
        return sentence.replaceAll("[^a-zA-Z1-9]", " ").toLowerCase();
    }
}
