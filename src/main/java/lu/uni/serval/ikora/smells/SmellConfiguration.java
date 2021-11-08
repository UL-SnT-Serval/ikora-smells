package lu.uni.serval.ikora.smells;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lu.uni.serval.ikora.core.analytics.clones.Clones;
import lu.uni.serval.ikora.core.model.KeywordDefinition;

public class SmellConfiguration {
    @JsonProperty(value = "maximum step size")
    private int maximumStepSize = 13;
    @JsonProperty(value = "maximum locator size")
    private int maximumLocatorSize = 1;
    @JsonProperty(value = "maximum setup size")
    private int maximumSetupSize = 10;
    @JsonProperty(value = "eager test threshold")
    private double eagerTestThreshold = 0.5;
    @JsonProperty(value = "assertion density threshold")
    private double assertionDensityThreshold;
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

    @JsonProperty(value = "maximum locator size")
    public int getMaximumLocatorSize() {
        return this.maximumLocatorSize;
    }

    @JsonProperty(value = "maximum locator size")
    public void setMaximumLocatorSize(int maximumLocatorSize) {
        this.maximumLocatorSize = maximumLocatorSize;
    }

    @JsonProperty(value = "maximum setup size")
    public int getMaximumSetupSize() {
        return maximumSetupSize;
    }

    @JsonProperty(value = "maximum setup size")
    public void setMaximumSetupSize(int maximumSetupSize) {
        this.maximumSetupSize = maximumSetupSize;
    }

    @JsonProperty(value = "eager test threshold")
    public double getEagerTestThreshold() {
        return eagerTestThreshold;
    }

    @JsonProperty(value = "eager test threshold")
    public void setEagerTestThreshold(double eagerTestThreshold) {
        this.eagerTestThreshold = eagerTestThreshold;
    }

    @JsonProperty(value = "assertion density threshold")
    public double getAssertionDensityThreshold() {
        return assertionDensityThreshold;
    }

    @JsonProperty(value = "assertion density threshold")
    public void setAssertionDensityThreshold(double assertionDensityThreshold) {
        this.assertionDensityThreshold = assertionDensityThreshold;
    }

    public void setClones(Clones<KeywordDefinition> clones){
        this.clones = clones;
    }

    public Clones<KeywordDefinition> getClones(){
        return this.clones;
    }
}
