package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.Literal;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.utils.LocatorUtils;
import tech.ikora.smells.visitors.ComplexLocatorVisitor;

import java.util.Optional;
import java.util.Set;

public class ComplexLocatorCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getComplexLocators() / (double)visitor.getLocators();

        return new SmellResult(SmellMetric.Type.COMPLEX_LOCATORS, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(edit.getLeft());
        final Optional<SourceNode> newNode = NodeUtils.toSourceNode(edit.getRight());

        return oldNode.isPresent()
                && newNode.isPresent()
                && nodes.contains(oldNode.get())
                && Literal.class.isAssignableFrom(newNode.get().getClass())
                && !LocatorUtils.isComplex(newNode.get().getName(), 4);
    }
}
