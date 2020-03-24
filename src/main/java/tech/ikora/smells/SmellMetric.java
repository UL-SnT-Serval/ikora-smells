package tech.ikora.smells;

public class SmellMetric {
    public enum Type{
        EAGER_TEST,
        RESOURCE_OPTIMISM,
        HARD_CODED_VALUES,
        CONDITIONAL_TEST_LOGIC,
        TEST_CODE_DUPLICATION,
        MISSING_ASSERTION,
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
}
