package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.core.analytics.visitor.FileMemory;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.SourceFile;
import lu.uni.serval.ikora.core.model.SourceNode;
import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ComplexLocatorVisitor;

import lu.uni.serval.ikora.core.model.TestCase;

import java.util.Set;

public class ComplexLocatorCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        ComplexLocatorVisitor visitor = visit(testCase, new PathMemory(), configuration);

        double rawValue = visitor.getComplexLocators();
        double normalizedValue = rawValue / visitor.getLocators();

        return new SmellResult(SmellMetric.Type.COMPLEX_LOCATORS, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public Set<SourceNode> collectInstances(SourceFile file, SmellConfiguration configuration) {
        return visit(file, new FileMemory(file), configuration).getNodes();
    }

    private ComplexLocatorVisitor visit(SourceNode node, VisitorMemory memory, SmellConfiguration configuration){
        final ComplexLocatorVisitor visitor = new ComplexLocatorVisitor(configuration.getMaximumLocatorSize());
        visitor.visit(node, memory);

        return visitor;
    }
}
