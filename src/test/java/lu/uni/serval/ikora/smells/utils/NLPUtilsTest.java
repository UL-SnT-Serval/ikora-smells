package lu.uni.serval.ikora.smells.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NLPUtilsTest {
    @Test
    void testIsUsingPersonalPronoun(){
        final String sentence = "Hello, I am a test";
        assertTrue(NLPUtils.isUsingPersonalPronoun(sentence));
    }

    @Test
    void testIsNotUsingPersonalPronoun(){
        final String sentence = "Hello, testers always be testing";
        assertFalse(NLPUtils.isUsingPersonalPronoun(sentence));
    }

    @Test
    void testUsingPersonalPronounInEmptySentence(){
        final String sentence = "";
        assertFalse(NLPUtils.isUsingPersonalPronoun(sentence));
    }
}