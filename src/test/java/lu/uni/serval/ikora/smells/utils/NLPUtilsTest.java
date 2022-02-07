package lu.uni.serval.ikora.smells.utils;

/*-
 * #%L
 * Ikora Smells
 * %%
 * Copyright (C) 2020 - 2022 University of Luxembourg
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
