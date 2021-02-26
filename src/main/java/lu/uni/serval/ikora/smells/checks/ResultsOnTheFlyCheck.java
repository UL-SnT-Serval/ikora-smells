package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.ResultOnTheFlyVisitor;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;

import java.util.Set;

public class ResultsOnTheFlyCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final ResultOnTheFlyVisitor visitor = new ResultOnTheFlyVisitor();
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getOnTheFly();
        double normalizedValue = rawValue / visitor.getExpected();

        return new SmellResult(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        if(edit.getLeft() == null){
            return false;
        }

        if(Variable.class.isAssignableFrom(edit.getLeft().getClass())){
            return SmellCheck.isFix(edit, nodes, Edit.Type.CHANGE_VALUE_TYPE);
        }

        if(edit.getLeft() instanceof Assignment){
            return SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_STEP);
        }

        return false;
    }
}
