/**
 * Copyright (c) 2009-2015, Christer Sandberg
 */
package lu.uni.serval.ikora.smells.utils.css.parser;

/**
 * Exception thrown on parsing errors.
 *
 * @author Christer Sandberg
 */
public class ParserException extends RuntimeException {

    /**
     * Create a new parser exception with the specified message.
     *
     * @param message The error message.
     */
    public ParserException(String message) {
        super(message);
    }

}
