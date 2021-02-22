package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.LackOfDocumentationVisitor;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;

import java.util.Set;

public class LackOfDocumentationCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        LackOfDocumentationVisitor visitor = new LackOfDocumentationVisitor();
        visitor.visit(testCase, new PathMemory());

        final int documented = visitor.getDocumentedKeyword();
        final int total = visitor.getTotalKeywords();
        final double metric = (double)documented / (double)total;

        return new SmellResult(SmellMetric.Type.LACK_OF_DOCUMENTATION, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(edit, nodes, Edit.Type.ADD_DOCUMENTATION);
    }
}
