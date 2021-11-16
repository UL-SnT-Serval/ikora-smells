package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.*;

public class SmellResults implements Iterable<SmellResult> {
    private final EnumMap<SmellMetric.Type, SmellResult> results;

    public SmellResults(){
        this.results = new EnumMap<>(SmellMetric.Type.class);
    }

    public void add(SmellResult smellResult){
        this.results.put(smellResult.getType(), smellResult);
    }

    public int getNumberMetrics() {
        return results.size();
    }

    public double getRawValue(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Double.NaN;
        }

        return smellResult.getRawValue();
    }

    public double getNormalizedValue(SmellMetric.Type type){
        final SmellResult smellResult = results.get(type);

        if(smellResult == null){
            return Double.NaN;
        }

        return smellResult.getNormalizedValue();
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
