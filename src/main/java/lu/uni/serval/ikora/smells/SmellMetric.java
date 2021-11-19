package lu.uni.serval.ikora.smells;

public class SmellMetric {
    public enum Type{
        ARMY_OF_CLONES,
        COMPLICATED_SETUP_SCENARIOS,
        CONDITIONAL_ASSERTION,
        CONDITIONAL_TEST_LOGIC,
        EAGER_TEST,
        HARDCODED_ENVIRONMENT_CONFIGURATIONS,
        HARD_CODED_VALUES,
        HIDING_TEST_DATA,
        LACK_OF_ENCAPSULATION,
        LONG_TEST_STEPS,
        MIDDLE_MAN,
        MISSING_ASSERTION,
        MISSING_DOCUMENTATION,
        NARCISSISTIC,
        NOISY_LOGGING,
        ON_THE_FLY,
        OVER_CHECKING,
        SAME_DOCUMENTATION,
        SENSITIVE_LOCATOR,
        SNEAKY_CHECKING,
        STINKY_SYNCHRONIZATION_SYNDROME,
        TRANSITIVE_IMPORT
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
