package tech.ikora.smells;

import tech.ikora.model.SourceNode;
import tech.ikora.smells.SmellMetric;

import java.util.Set;

public class SmellResult {
    final SmellMetric metric;
    final Set<SourceNode> nodes;

    public SmellResult(SmellMetric.Type type, double metric, Set<SourceNode> nodes) {
        this.metric = new SmellMetric(type, metric);
        this.nodes = nodes;
    }

    public SmellMetric.Type getType(){
        return this.metric.getType();
    }

    public double getValue(){
        return this.metric.getValue();
    }

    public Set<SourceNode> getNodes(){
        return this.nodes;
    }
}
