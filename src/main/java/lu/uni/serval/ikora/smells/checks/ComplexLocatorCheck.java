package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.utils.LocatorUtils;
import lu.uni.serval.ikora.smells.visitors.ComplexLocatorVisitor;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.Literal;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.smells.*;

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
        return nodes.contains(edit.getLeft())
                && Literal.class.isAssignableFrom(edit.getRight().getClass())
                && !LocatorUtils.isComplex(edit.getRight().getName(), 4);
    }
}
