package tech.ikora.smells.checks;

import tech.ikora.analytics.Action;
import tech.ikora.analytics.Difference;
import tech.ikora.analytics.visitor.PathMemory;
import tech.ikora.model.SourceNode;
import tech.ikora.model.TestCase;
import tech.ikora.smells.*;
import tech.ikora.smells.visitors.SleepCounterVisitor;

import java.util.Set;

public class StinkySynchronizationSyndromeCheck implements SmellCheck {
    @Override
    public SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration) {
        final SleepCounterVisitor visitor = new SleepCounterVisitor();
        visitor.visit(testCase, new PathMemory());

        final int sleepCalls = visitor.getSleepCalls();
        final int syncCalls = visitor.getSyncCalls();
        final double metric = (double)sleepCalls / (double)syncCalls;

        return new SmellResult(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, metric, visitor.getNodes());
    }

    @Override
    public boolean isFix(Action action, Set<SourceNode> nodes, SmellConfiguration configuration) {
        return SmellCheck.isFix(action, nodes, Action.Type.REMOVE_STEP, Action.Type.CHANGE_STEP);
    }
}
