package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.DeadCodeVisitor;

import java.util.Collections;
import java.util.Set;

public class DeadCodeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        return new SmellResult(SmellMetric.Type.DEAD_CODE, 0, 0, Collections.emptySet());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new PathMemory()).getNodes();
    }

    private DeadCodeVisitor visit(SourceNode node, VisitorMemory memory){
        final DeadCodeVisitor visitor = new DeadCodeVisitor();
        visitor.visit(node, memory);

        return visitor;
    }
}
