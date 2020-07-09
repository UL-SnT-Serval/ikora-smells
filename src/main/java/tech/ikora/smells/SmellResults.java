package tech.ikora.smells;

import tech.ikora.model.SourceNode;

import java.util.*;

public class SmellResults implements Iterable<SmellResult> {
    private final Map<SmellMetric.Type, SmellResult> results;

    public SmellResults(){
        this.results = new HashMap<>();
    }

    public void add(SmellResult smellResult){
        this.results.put(smellResult.getType(), smellResult);
    }

    public double getValue(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Double.NaN;
        }

        return smellResult.getValue();
    }

    public Set<SourceNode> getNodes(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Collections.emptySet();
        }

        return smellResult.getNodes();
    }

    @Override
    public Iterator<SmellResult> iterator() {
        return results.values().iterator();
    }
}
