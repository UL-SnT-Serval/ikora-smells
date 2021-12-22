package lu.uni.serval.ikora.smells.utils;

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
