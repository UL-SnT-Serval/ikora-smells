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

import org.ejml.simple.SimpleMatrix;

import java.util.*;

public class WordFrequency {
    private final List<Map<String, Long>> words;

    public WordFrequency(int size){
        this.words = new ArrayList<>(size);

        for(int i = 0; i < size; ++i){
            this.words.add(new HashMap<>());
        }
    }

    public void addWords(int position, List<String> newWords){
        for(String newWord: newWords){
            addWord(position, newWord);
        }
    }

    public void addWord(int position, String word){
        for(int i = 0; i < this.words.size(); ++i){
            Map<String, Long> counterMap = this.words.get(i);

            if(i == position){
                counterMap.compute(word, (k, v) -> v == null ? 1L : v + 1L);
            }
            else{
                counterMap.putIfAbsent(word, 0L);
            }
        }
    }

    public List<SimpleMatrix> getFrequencyVectors(){
        if(this.words.isEmpty()){
            return Collections.emptyList();
        }

        final List<SimpleMatrix> vectors = new ArrayList<>(this.words.size());

        int size = this.words.get(0).size();
        final List<String> wordLabels = new ArrayList<>(this.words.get(0).keySet());

        for(Map<String, Long> frequencies: this.words){
            SimpleMatrix vector = new SimpleMatrix(1, size);

            int col = 0;
            for(String wordLabel: wordLabels){
                vector.set(0, col++, frequencies.get(wordLabel));
            }

            vectors.add(vector);
        }

        return vectors;
    }
}
