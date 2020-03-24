package tech.ikora.smells;

import tech.ikora.model.TestCase;

import java.util.*;

public class SmellDetector {
    private final static Map<SmellMetric.Type, SmellCheck> smellChecks;
    private final Set<SmellMetric.Type> smellsToDetect;

    static {
        smellChecks = new HashMap<>(5);

        smellChecks.put(SmellMetric.Type.EAGER_TEST, new EagerTestCheck());
        smellChecks.put(SmellMetric.Type.RESOURCE_OPTIMISM, new ResourceOptimismCheck());
        smellChecks.put(SmellMetric.Type.HARD_CODED_VALUES, new HardcodedValuesCheck());
        smellChecks.put(SmellMetric.Type.CONDITIONAL_TEST_LOGIC, new ConditionalTestLogic());
        smellChecks.put(SmellMetric.Type.LACK_OF_DOCUMENTATION, new LackOfDocumentationCheck());
    }

    public SmellDetector(Set<SmellMetric.Type> smellsToDetect){
        this.smellsToDetect = smellsToDetect;
    }

    public Set<SmellMetric> computeMetrics(TestCase testCase){
       Set<SmellMetric> metrics = new HashSet<>();

       for(SmellMetric.Type type: smellsToDetect){
            metrics.add(smellChecks.get(type).computeMetric(testCase));
       }

        return metrics;
    }
}
