package lu.uni.serval.ikora.smells;

import lu.uni.serval.ikora.smells.checks.*;

import lu.uni.serval.ikora.core.model.TestCase;

import java.util.*;

public class SmellDetector {
    private static final EnumMap<SmellMetric.Type, SmellCheck> smellChecks;
    private final Set<SmellMetric.Type> smellsToDetect;

    static {
        smellChecks = new EnumMap<>(SmellMetric.Type.class);

        smellChecks.put(SmellMetric.Type.ARMY_OF_CLONES, new ArmyOfClonesCheck());
        smellChecks.put(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, new ComplicatedSetupCheck());
        smellChecks.put(SmellMetric.Type.CONDITIONAL_ASSERTION, new ConditionalAssertionCheck());
        smellChecks.put(SmellMetric.Type.EAGER_TEST, new EagerTestCheck());
        smellChecks.put(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, new HardCodedEnvironmentConfigurationCheck());
        smellChecks.put(SmellMetric.Type.HARD_CODED_VALUES, new HardcodedValuesCheck());
        smellChecks.put(SmellMetric.Type.HIDING_TEST_DATA, new HidingTestDataCheck());
        smellChecks.put(SmellMetric.Type.LACK_OF_ENCAPSULATION, new LackOfEncapsulationCheck());
        smellChecks.put(SmellMetric.Type.LONG_TEST_STEPS, new LongTestStepsCheck());
        smellChecks.put(SmellMetric.Type.MIDDLE_MAN, new MiddleManCheck());
        smellChecks.put(SmellMetric.Type.MISSING_ASSERTION, new MissingAssertionCheck());
        smellChecks.put(SmellMetric.Type.MISSING_DOCUMENTATION, new MissingDocumentationCheck());
        smellChecks.put(SmellMetric.Type.NARCISSISTIC, new NarcissisticCheck());
        smellChecks.put(SmellMetric.Type.NOISY_LOGGING, new NoisyLoggingCheck());
        smellChecks.put(SmellMetric.Type.ON_THE_FLY, new OnTheFlyCheck());
        smellChecks.put(SmellMetric.Type.OVER_CHECKING, new OverCheckingCheck());
        smellChecks.put(SmellMetric.Type.SAME_DOCUMENTATION, new SameDocumentationCheck());
        smellChecks.put(SmellMetric.Type.SENSITIVE_LOCATOR, new SensitiveLocatorCheck());
        smellChecks.put(SmellMetric.Type.SNEAKY_CHECKING, new SneakyCheckingCheck());
        smellChecks.put(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, new StinkySynchronizationSyndromeCheck());
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

    public int getNumberMetrics(){
        return this.smellsToDetect.size();
    }

    public static SmellDetector all(){
        return new SmellDetector(smellChecks.keySet());
    }
}
