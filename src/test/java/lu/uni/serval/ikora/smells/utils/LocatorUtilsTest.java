package lu.uni.serval.ikora.smells.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocatorUtilsTest {
    @Test
    void testIsComplexWithEmpty(){
        assertFalse(LocatorUtils.isComplex("", 1));
    }

    @Test
    void testIsComplexWithInvalid(){
        assertFalse(LocatorUtils.isComplex("missing type of locator", 1));
    }

    @Test
    void testIsComplexWithSimpleXPath() {
        assertFalse(LocatorUtils.isComplex("xpath:book[price>35]", 1));
    }

    @Test
    void testIsComplexWithComplexXPath() {
        assertTrue(LocatorUtils.isComplex("xpath:/bookstore/book[price>35]/price", 1));
    }

    @Test
    void testIsComplexWithSimpleXPathForLargerTolerance() {
        assertFalse(LocatorUtils.isComplex("xpath:/bookstore/book[price>35]/price", 3));
    }

    @Test
    void testIsComplexWithSimpleCss() {
        assertFalse(LocatorUtils.isComplex("css:a[href=\"https://code.tutsplus.com\"]", 1));
    }

    @Test
    void testIsComplexWithComplexCss() {
        assertTrue(LocatorUtils.isComplex("css:div#container > ul", 1));
    }

    @Test
    void testIsComplexWithSimpleCssForLargerTolerance() {
        assertFalse(LocatorUtils.isComplex("css:div#container > ul", 2));
    }

    @Test
    void testIsComplexWithSimpleJquery() {
        assertFalse(LocatorUtils.isComplex("jquery:input:not([type='hidden']", 1));
    }

    @Test
    void testIsComplexWithComplexJquery() {
        assertTrue(LocatorUtils.isComplex("jquery:.active input:not([type='hidden']", 1));
    }

    @Test
    void testIsComplexWithSimpleJqueryForLargerTolerance() {
        assertFalse(LocatorUtils.isComplex("jquery:.your-container > input:not([type='hidden'])", 2));
    }

    @Test
    void testIsComplexWithCompositeJqueryComplex() {
        assertTrue(LocatorUtils.isComplex("jquery:.active input:not([type='hidden'], .your-container > input:not([type='hidden'])", 3));
    }

    @Test
    void testIsComplexWithCompositeJqueryNotComplex() {
        assertFalse(LocatorUtils.isComplex("jquery:.active input:not([type='hidden'], .your-container > input:not([type='hidden'])", 4));
    }
}