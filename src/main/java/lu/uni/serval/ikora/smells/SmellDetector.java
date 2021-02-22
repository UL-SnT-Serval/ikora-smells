package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.smells.checks.*;
import lu.uni.serval.ikora.analytics.difference.Edit;
import lu.uni.serval.ikora.model.SourceNode;
import lu.uni.serval.ikora.model.TestCase;
import lu.uni.serval.ikora.smells.checks.*;

import java.util.*;

public class SmellDetector {
    private final static Map<SmellMetric.Type, SmellCheck> smellChecks;
    private final Set<SmellMetric.Type> smellsToDetect;

    static {
        smellChecks = new HashMap<>(SmellMetric.Type.values().length);

        smellChecks.put(SmellMetric.Type.HARD_CODED_VALUES, new HardcodedValuesCheck());
        smellChecks.put(SmellMetric.Type.LONG_TEST_STEPS, new LongTestStepsCheck());
        smellChecks.put(SmellMetric.Type.TEST_CLONES, new TestClonesCheck());
        smellChecks.put(SmellMetric.Type.MIDDLE_MAN, new MiddleManCheck());
        smellChecks.put(SmellMetric.Type.LACK_OF_ENCAPSULATION, new LackOfEncapsulationCheck());
        smellChecks.put(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, new LoggingInFixtureCodeCheck());
        smellChecks.put(SmellMetric.Type.HIDING_TEST_DATA_IN_FIXTURE_CODE, new HidingTestDataInFixtureCodeCheck());
        smellChecks.put(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, new StinkySynchronizationSyndromeCheck());
        smellChecks.put(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, new ResultsOnTheFlyCheck());
        smellChecks.put(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, new ComplicatedSetupCheck());
        smellChecks.put(SmellMetric.Type.COMPLEX_LOCATORS, new ComplexLocatorCheck());
        smellChecks.put(SmellMetric.Type.EAGER_TEST, new EagerTestCheck());
        smellChecks.put(SmellMetric.Type.USING_PERSONAL_PRONOUN, new UsingPersonalPronounCheck());
        smellChecks.put(SmellMetric.Type.MISSING_ASSERTION, new MissingAssertionCheck());
        smellChecks.put(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, new HardCodedEnvironmentConfigurationCheck());
        smellChecks.put(SmellMetric.Type.CONDITIONAL_ASSERTION, new ConditionalAssertionCheck());
        smellChecks.put(SmellMetric.Type.OVER_CHECKING, new OverCheckingCheck());
        smellChecks.put(SmellMetric.Type.SNEAKY_CHECKING, new SneakyCheckingCheck());
    }

    public SmellDetector(Set<SmellMetric.Type> smellsToDetect){
        this.smellsToDetect = smellsToDetect;
    }

    public SmellResults computeMetrics(TestCase testCase, SmellConfiguration configuration){
        SmellResults results = new SmellResults();

       for(SmellMetric.Type type: smellsToDetect){
           results.add(smellChecks.get(type).computeMetric(testCase, configuration));
       }

        return results;
    }

    public static boolean isFix(SmellMetric.Type type, Set<SourceNode> nodes, Edit edit, SmellConfiguration configuration){
        return smellChecks.get(type).isFix(edit, nodes, configuration);
    }

    public static SmellDetector all(){
        return new SmellDetector(smellChecks.keySet());
    }
}
