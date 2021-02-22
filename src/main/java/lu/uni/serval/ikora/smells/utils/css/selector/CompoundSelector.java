/**
 * Copyright (c) 2009-2015, Christer Sandberg
 */
package lu.uni.serval.ikora.smells.utils.css.selector;

import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.util.Pair;

/**
 * A {@code CompoundSelector} groups a {@link SimpleSelector}
 * sequence and a pointer to the previous {@code CompoundSelector} separated by a
 * {@link Combinator} if any.
 * <p/>
 * See <a href="http://www.w3.org/TR/selectors/#sequence">http://www.w3.org/TR/selectors/#sequence</a>
 *
 * @author Christer Sandberg
 */
public class CompoundSelector {

    /** Simple selector sequence. */
    private final List<SimpleSelector> simpleSelectors;

    /** A pointer to the previous compound selector separated by a combinator or {@code null} */
    private final Pair<Combinator, CompoundSelector> previous;

    /**
     * Create a new compound selector.
     *
     * @param simpleSelectors A simple selector sequence.
     * @param previous A pointer to the previous compound selector separated by a combinator or {@code null}
     */
    public CompoundSelector(List<SimpleSelector> simpleSelectors, Pair<Combinator, CompoundSelector> previous) {
        this.simpleSelectors = simpleSelectors;
        this.previous = previous;
    }

    /**
     * Creates a new compound selector without any associated previous selector.
     *
     * @param simpleSelectors A simple selector sequence.
     * @return A new compound selector.
     */
    public static CompoundSelector of(List<SimpleSelector> simpleSelectors) {
        return new CompoundSelector(simpleSelectors, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        CompoundSelector that = (CompoundSelector) other;
        return Objects.equals(simpleSelectors, that.simpleSelectors) &&
                Objects.equals(previous, that.previous);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(simpleSelectors, previous);
    }


    public List<SimpleSelector> getSimpleSelectors() {
        return simpleSelectors;
    }

    public Pair<Combinator, CompoundSelector> getPrevious() {
        return previous;
    }
}
