package lu.uni.serval.ikora.smells.visitors;

import lu.uni.serval.ikora.core.analytics.visitor.VisitorMemory;
import lu.uni.serval.ikora.core.model.UserKeyword;
import lu.uni.serval.ikora.core.model.VariableAssignment;

public class DeadCodeVisitor extends SmellVisitor{
    @Override
    public void visit(UserKeyword keyword, VisitorMemory memory) {
        if(keyword.getDependencies().isEmpty()){
            addNode(keyword);
        }
    }

    @Override
    public void visit(VariableAssignment variableAssignment, VisitorMemory memory) {
        if(variableAssignment.getDependencies().isEmpty()){
            addNode(variableAssignment);
        }
    }
}
