/**
 * Copyright (c) 2009-2015, Christer Sandberg
 */
package tech.ikora.smells.utils.css.selector;

import java.util.Objects;

/**
 * Represents a selector.
 * <p/>
 * See <a href="http://www.w3.org/TR/selectors/#selector-syntax">http://www.w3.org/TR/selectors/#selector-syntax</a>
 *
 * @author Christer Sandberg
 */
public class Selector {

    /** The last compound selector in the sequence. */
    private final CompoundSelector compoundSelector;

    /** The pseudo element or {@code null} */
    private final PseudoElementSelector pseudoElement;

    /**
     * Create a new selector.
     *
     * @param compoundSelector The last compound selector in the sequence.
     * @param pseudoElement The pseudo element or {@code null}
     */
    public Selector(CompoundSelector compoundSelector, PseudoElementSelector pseudoElement) {
        this.compoundSelector = compoundSelector;
        this.pseudoElement = pseudoElement;
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

        Selector selector = (Selector) other;
        return Objects.equals(compoundSelector, selector.compoundSelector) &&
                Objects.equals(pseudoElement, selector.pseudoElement);
    }


    public CompoundSelector getCompoundSelector() {
        return compoundSelector;
    }

    public PseudoElementSelector getPseudoElement() {
        return pseudoElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(compoundSelector, pseudoElement);
    }

}
