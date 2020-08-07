package tech.ikora.smells.checks;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.*;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.OneActionVisitor;
import tech.ikora.utils.Ast;

import java.util.Optional;
import java.util.Set;

public class MiddleManCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        OneActionVisitor visitor = new OneActionVisitor(Keyword.Type.USER);
        visitor.visit(testCase, new PathMemory());

        final int singleActionCount = visitor.getSingleActionCount();
        final int keywordsCounts = visitor.getKeywordsCount();
        final double metric = (double)singleActionCount / (double)keywordsCounts;

        return new SmellResult(SmellMetric.Type.MIDDLE_MAN, metric, visitor.getNodes());
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
