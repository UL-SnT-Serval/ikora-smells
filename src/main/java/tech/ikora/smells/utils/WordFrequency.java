package tech.ikora.smells.utils;

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
        List<SimpleMatrix> vectors = new ArrayList<>(this.words.size());

        int size = this.words.get(0).size();
        List<String> words = new ArrayList<>(this.words.get(0).keySet());

        for(Map<String, Long> frequencies: this.words){
            SimpleMatrix vector = new SimpleMatrix(1, size);

            int col = 0;
            for(String word: words){
                vector.set(0, col++, frequencies.get(word));
            }

            vectors.add(vector);
        }

        return vectors;
    }
}
