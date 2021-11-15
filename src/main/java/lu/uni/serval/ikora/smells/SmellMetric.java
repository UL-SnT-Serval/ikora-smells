package lu.uni.serval.ikora.smells;

public class SmellMetric {
    public enum Type{
        HARD_CODED_VALUES,
        CONDITIONAL_TEST_LOGIC,
        LONG_TEST_STEPS,
        TEST_CLONES,
        MIDDLE_MAN,
        LACK_OF_ENCAPSULATION,
        LOGGING_IN_FIXTURE_CODE,
        HIDING_TEST_DATA_IN_FIXTURE_CODE,
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
        MISSING_DOCUMENTATION,
        SAME_DOCUMENTATION
    }

    private final Type type;
    private final double rawValue;
    private final double normalizedValue;

    public SmellMetric(Type type, double rawValue, double normalizedValue) {
        this.type = type;
        this.rawValue = rawValue;
        this.normalizedValue = normalizedValue;
    }

    public Type getType() {
        return type;
    }

    public double getRawValue() {
        return rawValue;
    }

    public double getNormalizedValue() {
        return normalizedValue;
    }

    public static SmellMetric nan(Type type){
        return new SmellMetric(type, Double.NaN, Double.NaN);
    }
}
