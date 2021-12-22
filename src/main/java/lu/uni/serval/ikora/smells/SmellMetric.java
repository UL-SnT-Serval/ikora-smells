package lu.uni.serval.ikora.smells;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class SmellMetric {
    public enum Type{
        ARMY_OF_CLONES,
        COMPLICATED_SETUP_SCENARIOS,
        CONDITIONAL_ASSERTION,
        DEAD_CODE,
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
