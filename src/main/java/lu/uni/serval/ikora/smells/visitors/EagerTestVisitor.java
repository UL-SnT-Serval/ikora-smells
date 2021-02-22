package lu.uni.serval.ikora.smells.visitors;

import edu.stanford.nlp.simple.Sentence;
import lu.uni.serval.ikora.smells.utils.WordFrequency;
import org.ejml.simple.SimpleMatrix;
import lu.uni.serval.ikora.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.model.UserKeyword;

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
