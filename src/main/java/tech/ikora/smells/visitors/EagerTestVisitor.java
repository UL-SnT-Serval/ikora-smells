package tech.ikora.smells.visitors;

import edu.stanford.nlp.simple.Sentence;
import org.ejml.simple.SimpleMatrix;
import tech.ikora.analytics.visitor.TreeVisitor;
import tech.ikora.analytics.visitor.VisitorMemory;
import tech.ikora.analytics.visitor.VisitorUtils;
import tech.ikora.model.UserKeyword;
import tech.ikora.smells.utils.WordFrequency;

import java.util.List;

public class EagerTestVisitor extends TreeVisitor {
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
        final String name = keyword.getName().getText().toLowerCase();
        final String cleanName = clean(name);
        final Sentence sentence = new Sentence(cleanName);

        wordFrequency.addWords(position, sentence.lemmas());

        VisitorUtils.traverseSteps(this, keyword, memory);
    }

    private String clean(String sentence){
        return sentence.replaceAll("[^a-zA-Z1-9]", " ").toLowerCase();
    }
}
