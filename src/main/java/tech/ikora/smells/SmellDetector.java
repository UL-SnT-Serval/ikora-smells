package tech.ikora.smells;

import tech.ikora.model.TestCase;
import tech.ikora.smells.checks.*;

import java.util.*;

public class SmellDetector {
    private final static Map<SmellMetric.Type, SmellCheck> smellChecks;
    private final Set<SmellMetric.Type> smellsToDetect;

    static {
        smellChecks = new HashMap<>(SmellMetric.Type.values().length);

        smellChecks.put(SmellMetric.Type.OBSCURE_TEST, new ObscureTestCheck());
        smellChecks.put(SmellMetric.Type.LONG_TEST_STEPS, new LongTestStepsCheck());
        smellChecks.put(SmellMetric.Type.TEST_CLONES, new TestClonesCheck());
        smellChecks.put(SmellMetric.Type.MIDDLE_MAN, new MiddleManCHeck());
        smellChecks.put(SmellMetric.Type.LACK_OF_ENCAPSULATION, new LackOfEncapsulationCheck());
        smellChecks.put(SmellMetric.Type.LOGGING_IN_FIXTURE_CODE, new LoggingInFixtureCodeCheck());
        smellChecks.put(SmellMetric.Type.HIDING_TEST_DATA_IN_FIXTURE_CODE, new HidingTestDateInFixtureCodeCheck());
        smellChecks.put(SmellMetric.Type.IMPLEMENTATION_DEPENDENT, new ImplementationDependentCheck());
        smellChecks.put(SmellMetric.Type.STINKY_SYNCHRONIZATION_SYNDROME, new StinkySynchronizationCheck());
        smellChecks.put(SmellMetric.Type.CALCULATE_EXPECTED_RESULTS_ON_THE_FLY, new ResultsOnTheFlyCheck());
        smellChecks.put(SmellMetric.Type.COMPLICATED_SETUP_SCENARIOS, new ComplicatedSetupCheck());
        smellChecks.put(SmellMetric.Type.COMPLEX_SELECTORS, new ComplexSelectorsCheck());
        smellChecks.put(SmellMetric.Type.EAGER_TEST, new EagerTestCheck());
        smellChecks.put(SmellMetric.Type.USING_PERSONAL_PRONOUN, new UsingPersonalPronounCheck());
        smellChecks.put(SmellMetric.Type.MISSING_ASSERTION, new MissingAssertionCheck());
        smellChecks.put(SmellMetric.Type.HARDCODED_ENVIRONMENT_CONFIGURATIONS, new HardCodedEnvironmentConfiguration());
        smellChecks.put(SmellMetric.Type.CONDITIONAL_ASSERTION, new ConditionalAssertionCheck());
        smellChecks.put(SmellMetric.Type.OVER_CHECKING, new OverCheckingCheck());
        smellChecks.put(SmellMetric.Type.SNEAKY_CHECKING, new SneakyCheckingCheck());
        smellChecks.put(SmellMetric.Type.DATA_CREEP, new DataCreepCheck());
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

    public static SmellDetector all(){
        return new SmellDetector(smellChecks.keySet());
    }
}
