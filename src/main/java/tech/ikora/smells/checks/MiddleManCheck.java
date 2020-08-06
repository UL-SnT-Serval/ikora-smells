package tech.ikora.smells.checks;

import tech.ikora.analytics.Edit;
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
            final Optional<SourceNode> oldNode = NodeUtils.toSourceNode(edit.getLeft());
            final Optional<SourceNode> newNode = NodeUtils.toSourceNode(edit.getRight());

            if(!oldNode.isPresent() || !newNode.isPresent()){
                return false;
            }

            final Optional<UserKeyword> parent = Ast.getParentByType(oldNode.get(), UserKeyword.class);

            if(!parent.isPresent() || !nodes.contains(parent.get())){
                return false;
            }

            if(!Step.class.isAssignableFrom(newNode.get().getClass())){
                return false;
            }

            return ((Step) newNode.get()).getKeywordCall()
                    .filter(call -> call.getKeywordType() != Keyword.Type.USER).isPresent();
        }

        return false;
    }
}
