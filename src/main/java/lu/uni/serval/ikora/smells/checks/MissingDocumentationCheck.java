package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.analytics.visitor.FileMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;

import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.TestCase;
import lu.uni.serval.ikora.smells.visitors.MissingDocumentationVisitor;

import java.util.Set;

public class MissingDocumentationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final MissingDocumentationVisitor visitor = visit(testCase, new PathMemory());
        final double rawValue = visitor.getUndocumentedKeywords();
        final double normalizedValue = rawValue / visitor.getTotalKeywords();

        return new SmellResult(SmellMetric.Type.MISSING_DOCUMENTATION, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file)).getNodes();
    }

    private MissingDocumentationVisitor visit(SourceNode node, VisitorMemory memory){
        final MissingDocumentationVisitor visitor = new MissingDocumentationVisitor();
        visitor.visit(node, memory);

        return visitor;
    }
}
