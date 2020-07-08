package tech.ikora.smells.checks;

import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.TestCase;
import tech.ikora.smells.SmellCheck;
import tech.ikora.smells.SmellDetector;
import tech.ikora.smells.SmellMetric;
import tech.ikora.smells.visitors.ComplexLocatorVisitor;
import tech.ikora.smells.SmellResult;

public class ComplexLocatorCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellDetector detector) {
        ComplexLocatorVisitor visitor = new ComplexLocatorVisitor();
        visitor.visit(testCase, new PathMemory());

        double metric = (double)visitor.getComplexLocators() / (double)visitor.getLocators();

        return new SmellResult(SmellMetric.Type.COMPLEX_LOCATORS, metric, visitor.getNodes());
    }
}
