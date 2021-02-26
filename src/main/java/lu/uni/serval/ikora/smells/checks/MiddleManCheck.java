package lu.uni.serval.ikora.smells.checks;

import lu.uni.serval.ikora.smells.SmellCheck;
import lu.uni.serval.ikora.smells.SmellConfiguration;
import lu.uni.serval.ikora.smells.SmellMetric;
import lu.uni.serval.ikora.smells.SmellResult;
import lu.uni.serval.ikora.smells.visitors.OneActionVisitor;

import lu.uni.serval.ikora.core.analytics.difference.Edit;
import lu.uni.serval.ikora.core.analytics.visitor.PathMemory;
import lu.uni.serval.ikora.core.model.*;
import lu.uni.serval.ikora.core.utils.Ast;

import java.util.Optional;
import java.util.Set;

public class MiddleManCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.USER);
        visitor.visit(testCase, new PathMemory());

        double rawValue = visitor.getSingleActionCount();
        double normalizedValue = rawValue / visitor.getKeywordsCount();

        return new SmellResult(SmellMetric.Type.MIDDLE_MAN, rawValue, normalizedValue, visitor.getNodes());
    }

    @Override
    public boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration) {
        if(SmellCheck.isFix(edit, nodes, Edit.Type.REMOVE_USER_KEYWORD)){
            return true;
        }

        if(edit.getType() == Edit.Type.CHANGE_STEP){
            final Optional<UserKeyword> parent = Ast.getParentByType(edit.getLeft(), UserKeyword.class);

            if(!parent.isPresent() || !nodes.contains(parent.get())){
                return false;
            }

            if(!Step.class.isAssignableFrom(edit.getRight().getClass())){
                return false;
            }

            return ((Step)edit.getRight()).getKeywordCall()
                    .filter(call -> call.getKeywordType() != Keyword.Type.USER).isPresent();
        }

        return false;
    }
}
