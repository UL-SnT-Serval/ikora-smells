package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.core.model.SourceNode;

import java.util.Set;

public class SmellResult {
    final SmellMetric metric;
    final Set<SourceNode> nodes;

    public SmellResult(SmellMetric.Type type, double rawValue, double normalizedValue, Set<SourceNode> nodes) {
        this.metric = new SmellMetric(type, rawValue, normalizedValue);
        this.nodes = nodes;
    }

    public SmellMetric.Type getType(){
        return this.metric.getType();
    }

    public double getRawValue(){
        return this.metric.getRawValue();
    }

    public double getNormalizedValue(){
        return this.metric.getNormalizedValue();
    }

    public Set<SourceNode> getNodes(){
        return this.nodes;
    }
}
