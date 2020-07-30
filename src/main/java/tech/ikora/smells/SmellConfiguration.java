package tech.ikora.smells;

import tech.ikora.analytics.clones.Clones;
import tech.ikora.model.UserKeyword;

public class SmellConfiguration {
    private Clones<UserKeyword> clones = new Clones<>();
    private int maximumStepSize = 30;

    public void setClones(Clones<UserKeyword> clones){
        this.clones = clones;
    }

    public Clones<UserKeyword> getClones(){
        return this.clones;
    }

    public int getMaximumStepSize() {
        return maximumStepSize;
    }

    public void setMaximumStepSize(int maximumStepSize) {
        this.maximumStepSize = maximumStepSize;
    }
}
