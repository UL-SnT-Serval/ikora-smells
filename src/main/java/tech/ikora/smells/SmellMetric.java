package tech.ikora.smells;

public class SmellMetric {
    public enum Type{
        OBSCURE_TEST,
        HARD_CODED_VALUES,
        CONDITIONAL_TEST_LOGIC,
        LONG_TEST_STEPS,
        TEST_CLONES,
        MIDDLE_MAN,
        LACK_OF_ENCAPSULATION,
        LOGGING_IN_FIXTURE_CODE,
        HIDING_TEST_DATA_IN_FIXTURE_CODE,
        IMPLEMENTATION_DEPENDENT,
        STINKY_SYNCHRONIZATION_SYNDROME,
        CALCULATE_EXPECTED_RESULTS_ON_THE_FLY,
        COMPLICATED_SETUP_SCENARIOS,
        COMPLEX_LOCATORS,
        EAGER_TEST,
        USING_PERSONAL_PRONOUN,
        MISSING_ASSERTION,
        HARDCODED_ENVIRONMENT_CONFIGURATIONS,
        CONDITIONAL_ASSERTION,
        OVER_CHECKING,
        SNEAKY_CHECKING,
        DATA_CREEP,
        LACK_OF_DOCUMENTATION
    }

    private final Type type;
    private final double value;

    public SmellMetric(Type type, double value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public static SmellMetric nan(Type type){
        return new SmellMetric(type, Double.NaN);
    }
}
