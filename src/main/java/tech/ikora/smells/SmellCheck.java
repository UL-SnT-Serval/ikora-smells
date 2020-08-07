package tech.ikora.smells;

import tech.ikora.analytics.difference.Edit;
import tech.ikora.model.*;

import java.util.Arrays;
import java.util.Set;

public interface SmellCheck {
    SmellResult computeMetric(TestCase testCase, SmellConfiguration configuration);
    boolean isFix(Edit edit, Set<SourceNode> nodes, SmellConfiguration configuration);

    static boolean isFix(Edit edit, Set<SourceNode> nodes, Edit.Type... types){
        if(Arrays.stream(types).noneMatch(t -> edit.getType() == t)){
            return false;
        }

        return nodes.contains(edit.getLeft());
    }
}
