package lu.uni.serval.ikora.smells;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.model.KeywordDefinition;

public class SmellConfiguration {
    @JsonProperty(value = "maximum step size")
    private int maximumStepSize = 30;
    @JsonIgnore
    private Clones<KeywordDefinition> clones = new Clones<>();

    @JsonProperty(value = "maximum step size")
    public int getMaximumStepSize() {
        return maximumStepSize;
    }

    @JsonProperty(value = "maximum step size")
    public void setMaximumStepSize(int maximumStepSize) {
        this.maximumStepSize = maximumStepSize;
    }

    public void setClones(Clones<KeywordDefinition> clones){
        this.clones = clones;
    }

    public Clones<KeywordDefinition> getClones(){
        return this.clones;
    }
}
