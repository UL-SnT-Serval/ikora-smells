package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.analytics.visitor.FileMemory;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.TransitiveImportVisitor;

import java.util.Set;

public class TransitiveImportCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final TransitiveImportVisitor visitor = visit(testCase, new PathMemory());
        double rawValue = visitor.getNodes().size();
        double normalizedValue = rawValue / visitor.getNumberCalls();

        return new SmellResult(SmellMetric.Type.TRANSITIVE_IMPORT, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file)).getNodes();
    }

    private TransitiveImportVisitor visit(SourceNode node, VisitorMemory memory){
        final TransitiveImportVisitor visitor = new TransitiveImportVisitor();
        visitor.visit(node, memory);

        return visitor;
    }
}
