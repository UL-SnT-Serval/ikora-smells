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

import java.util.Arrays;
import java.util.regex.Pattern;

public class LocatorUtils {
    private static final Pattern xPathPattern =  Pattern.compile("^xpath:", Pattern.CASE_INSENSITIVE);
    private static final Pattern cssPattern =  Pattern.compile("^css:", Pattern.CASE_INSENSITIVE);
    private static final Pattern jqueryPattern =  Pattern.compile("^jquery:", Pattern.CASE_INSENSITIVE);

    private LocatorUtils() {}

    public static boolean isComplex(final String value, int maxSize){
        if(xPathPattern.matcher(value).find()){
            return getXPathSize(value.replaceAll(xPathPattern.pattern(), "")) > maxSize;
        }
        else if(cssPattern.matcher(value).find()){
            return getCssSize(value.replaceAll(cssPattern.pattern(), "")) > maxSize;
        }
        else if(jqueryPattern.matcher(value).find()){
            return getJquerySize(value.replaceAll(jqueryPattern.pattern(), "")) > maxSize;
        }

        // all other strategies do not generate complex locators
        return false;
    }

    private static int getXPathSize(final String value) {
        return value.replaceAll("^(//|/)", "").split("/").length;
    }

    private static int getCssSize(final String value){
        return value.split(">").length;
    }

    private static int getJquerySize(final String value){
        final String[] split = value.trim()
                .replaceAll("(?:([\"'`])[^\1]*?\1)\\s+|\\r?\\n", "")
                .split("[.,\\s>#]");

        return (int)Arrays.stream(split).filter(s -> !s.isEmpty()).count();
    }
}
