/**
 * Copyright (c) 2009-2015, Christer Sandberg
 */
package lu.uni.serval.ikora.smells.utils.css.parser;

import lu.uni.serval.ikora.smells.utils.css.selector.*;
import lu.uni.serval.ikora.smells.utils.css.tokenizer.Token;
import lu.uni.serval.ikora.smells.utils.css.tokenizer.TokenType;
import lu.uni.serval.ikora.smells.utils.css.tokenizer.Tokenizer;
import org.apache.commons.math3.util.Pair;
import lu.uni.serval.ikora.smells.utils.css.selector.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Selector parser.
 *
 * @author Christer Sandberg
 */
public class SelectorParser {

    /** Tokenizer used when parsing. */
    private final Tokenizer tokenizer;

    /** Possibly saved token. */
    private Token savedToken;

    /**
     * Create a new selector parser.
     *
     * @param tokenizer Tokenizer used when parsing.
     */
    private SelectorParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Parse selectors from the given string.
     *
     * @param str The selectors string.
     * @return The selectors parsed.
     */
    public static Selector parse(String str) {
        return parse(new Tokenizer(str));
    }

    /**
     * Parse selectors from the given tokenizer.
     *
     * @param tokenizer The tokenizer to use when parsing.
     * @return The selectors parsed.
     */
    public static Selector parse(Tokenizer tokenizer) {
        return new SelectorParser(tokenizer).parseSelector();
    }

    /**
     * Parse a selector.
     * <p/>
     * See http://www.w3.org/TR/selectors/#selector-syntax
     *
     * @return The selector parsed.
     */
    private Selector parseSelector() {
        Pair<List<SimpleSelector>, PseudoElementSelector> simpleSelectors = parseSimpleSelectors();
        CompoundSelector compoundSelector = CompoundSelector.of(simpleSelectors.getFirst());
        PseudoElementSelector pseudoElement = simpleSelectors.getSecond();
        while (pseudoElement == null) {
            Pair<Token, Boolean> p = skipWhitespace();
            if (p.getFirst().type == TokenType.EOF) {
                break;
            } else if (p.getFirst().type == TokenType.COMMA) {
                savedToken = p.getFirst();
                break;
            }

            Combinator combinator = null;
            if (p.getFirst().type == TokenType.DELIM) {
                switch (p.getFirst().value) {
                case ">":
                    combinator = Combinator.CHILD;
                    break;
                case "+":
                    combinator = Combinator.NEXT_SIBLING;
                    break;
                case "~":
                    combinator = Combinator.LATER_SIBLING;
                    break;
                }
            }

            if (combinator == null) {
                if (p.getSecond()) {
                    combinator = Combinator.DESCENDANT;
                } else {
                    throw expected("one of ' ', '>', '+', '~'", p.getFirst());
                }

                savedToken = p.getFirst();
            }

            simpleSelectors = parseSimpleSelectors();
            compoundSelector = new CompoundSelector(simpleSelectors.getFirst(), new Pair<>(combinator, compoundSelector));
        }

        return new Selector(compoundSelector, pseudoElement);
    }

    /**
     * Parse a sequence of simple selectors.
     * <p/>
     * On successful parsing it returns a sequence of selectors and maybe a pseudo element selector
     * indicating if the last (or only) simple selector in the sequence is a pseudo element.
     * <p/>
     * See http://www.w3.org/TR/selectors/#sequence
     *
     * @return A sequence of simple selectors parsed and a pseudo element selector or {@code null}
     */
    private Pair<List<SimpleSelector>, PseudoElementSelector> parseSimpleSelectors() {
        int pos = tokenizer.getPosition();

        LinkedList<SimpleSelector> selectorSequence = new LinkedList<>();
        PseudoElementSelector pseudoElement = null;

        boolean empty = true;
        Pair<String, Boolean> name = parseName();
        if (name.getSecond() && !"*".equals(name.getFirst())) {
            selectorSequence.add(new LocalNameSelector(name.getFirst()));
            empty = false;
        }

        while (true) {
            SimpleSelector selector = parseOneSimpleSelector(false);
            if (selector == null) {
                break;
            }

            if (selector instanceof PseudoElementSelector) {
                empty = false;
                pseudoElement = (PseudoElementSelector) selector;
                break;
            }

            selectorSequence.add(selector);
            empty = false;
        }

        if (empty && !name.getSecond()) {
            throw new IllegalArgumentException("No simple selectors found at position " + pos);
        }

        return new Pair<List<SimpleSelector>, PseudoElementSelector>(selectorSequence, pseudoElement);
    }

    /**
     * Parse the name of an element and returns the name and a boolean indicating
     * if a type selector was found or not.
     * <p/>
     * See http://www.w3.org/TR/selectors/#type-selectors and http://www.w3.org/TR/selectors/#universal-selector
     *
     * @return The parsed name and whether a type selector was found or not.
     */
    private Pair<String, Boolean> parseName() {
        Token token = skipWhitespace().getFirst();
        switch (token.type) {
        case DELIM:
            if ("*".equals(token.value)) {
                return new Pair<>("*", true);
            }

            break;
        case IDENT:
            return new Pair<>(token.value, true);
        }

        savedToken = token;
        return new Pair<>("*", false);
    }

    /**
     * Parse one simple selector (excluding the type selector).
     * <p/>
     * See http://www.w3.org/TR/selectors/#simple-selectors
     *
     * @param insideNegation If inside a negation selector.
     * @return The simple selector parsed or {@code null}
     */
    private SimpleSelector parseOneSimpleSelector(boolean insideNegation) {
        Token token = nextToken();
        switch (token.type) {
        case HASH:
            Token.Hash h = (Token.Hash) token;
            return new AttributeSelector(AttributeSelector.Match.EQUALS, "id", h.value);
        case DELIM:
            if (".".equals(token.value)) {
                token = nextToken();
                if (token.type == TokenType.IDENT) {
                    return new AttributeSelector(AttributeSelector.Match.INCLUDES, "class", token.value);
                } else {
                    throw expected("class value", token);
                }
            }

            throw expected(".", token);
        case LEFT_SQUARE_BRACKET:
            return parseAttribute();
        case COLON:
            token = nextToken();
            switch (token.type) {
            case IDENT:
                switch (token.value.toLowerCase()) {
                case "first-line":
                case "first-letter":
                case "before":
                case "after":
                    return new PseudoElementSelector(token.value);
                default:
                    return new PseudoClassSelector(token.value);
                }
            case COLON:
                token = nextToken();
                if (token.type != TokenType.IDENT) {
                    throw expected("pseudo element value", token);
                } else {
                    return new PseudoElementSelector(token.value);
                }
            case FUNCTION:
                return parseFunctionalPseudoClass(token.value, insideNegation);
            }
        }

        savedToken = token;
        return null;
    }

    /**
     * Parse an attribute selector.
     * <p/>
     * See http://www.w3.org/TR/selectors/#attribute-selectors
     *
     * @return The attribute selector parsed.
     */
    private AttributeSelector parseAttribute() {
        Token token = skipWhitespace().getFirst();
        if (token.type != TokenType.IDENT) {
            throw expected("attribute name", token);
        }

        String name = token.value;
        token = skipWhitespace().getFirst();
        if (token.type == TokenType.RIGHT_SQUARE_BRACKET) {
            return new AttributeSelector(AttributeSelector.Match.EXISTS, name, "");
        }

        AttributeSelector.Match match = null;
        switch (token.type) {
        case PREFIX_MATCH:
            match = AttributeSelector.Match.BEGINS;
            break;
        case SUFFIX_MATCH:
            match = AttributeSelector.Match.ENDS;
            break;
        case SUBSTRING_MATCH:
            match = AttributeSelector.Match.CONTAINS;
            break;
        case INCLUDE_MATCH:
            match = AttributeSelector.Match.INCLUDES;
            break;
        case DASH_MATCH:
            match = AttributeSelector.Match.HYPHENS;
            break;
        case DELIM:
            if ("=".equals(token.value)) {
                match = AttributeSelector.Match.EQUALS;
            } else {
                throw expected("=", token);
            }
        }

        token = skipWhitespace().getFirst();

        String value;
        if (token.type == TokenType.IDENT || token.type == TokenType.STRING) {
            value = token.value;
        } else {
            throw expected("attribute value", token);
        }

        token = skipWhitespace().getFirst();
        if (token.type != TokenType.RIGHT_SQUARE_BRACKET) {
            throw expected("]", token);
        }

        return new AttributeSelector(match, name, value);
    }

    /**
     * Parse a functional pseudo class.
     * <p/>
     * See http://www.w3.org/TR/selectors/#structural-pseudos
     *
     * @param name The functional pseudo class name.
     * @param insideNegation If inside a negation selector.
     * @return The simple selector parsed.
     */
    private SimpleSelector parseFunctionalPseudoClass(String name, boolean insideNegation) {
        int pos = tokenizer.getPosition();
        switch (name.toLowerCase()) {
        case "nth-child":
        case "nth-last-child":
        case "nth-of-type":
        case "nth-last-of-type":
            Pair<Integer, Integer> nth = NthParser.parse(tokenizer);
            return new PseudoNthSelector(name, nth.getFirst(), nth.getSecond());
        case "not":
            if (insideNegation) {
                throw new ParserException("Error at position " + pos + ": negations may not be nested");
            }

            PseudoNegationSelector selector;
            Pair<String, Boolean> pair = parseName();
            if (pair.getSecond()) {
                selector = new PseudoNegationSelector(new LocalNameSelector(pair.getFirst()));
            } else {
                SimpleSelector simpleSelector = parseOneSimpleSelector(true);
                if (simpleSelector == null) {
                    throw expected("simple selector", nextToken());
                }

                selector = new PseudoNegationSelector(simpleSelector);
            }

            Token token = skipWhitespace().getFirst();
            if (token.type != TokenType.RIGHT_PAREN) {
                throw expected(")", token);
            }

            return selector;
        }

        StringBuilder sb = new StringBuilder();
        while (true) {
            Token token = nextToken();
            if (token.type == TokenType.EOF) {
                throw new ParserException("EOF in function expression starting at position " + pos);
            } else if (token.type == TokenType.RIGHT_PAREN) {
                break;
            } else {
                sb.append(token.value);
            }
        }

        return new PseudoFunctionSelector(name, sb.toString());
    }

    /**
     * Returns the next token to parse.
     *
     * @return The next token.
     */
    private Token nextToken() {
        if (savedToken != null) {
            Token token = savedToken;
            savedToken = null;
            return token;
        }

        return tokenizer.nextToken();
    }

    /**
     * Skips whitespace tokens and returns the next non-whitespace token and a
     * boolean inidicating if some whitespace was skipped.
     *
     * @return A pair of the next non-whitespace token and whether some whitespace was skipped.
     */
    private Pair<Token, Boolean> skipWhitespace() {
        boolean skipped = false;
        while (true) {
            Token token = nextToken();
            if (token.type != TokenType.WHITESPACE) {
                return new Pair<>(token, skipped);
            }

            skipped = true;
        }
    }

    /**
     * Returns an exception of what was expected and what was unexpectedly found.
     *
     * @param what What was expected.
     * @param token The token found.
     * @return An exception.
     */
    private static ParserException expected(String what, Token token) {
        String msg = String.format("Expected %s at position %d, got %s", what, token.position, token.type);
        return new ParserException(msg);
    }

}
